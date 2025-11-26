package com.repzone.presentation.legacy.viewmodel.productlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.data.repository.product.ProductPagingSource
import com.repzone.domain.model.product.ProductRowState
import com.repzone.domain.document.base.AddLineResult
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ProductListValidator
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.ValidationStatus
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.repository.IProductRepository
import com.repzone.presentation.legacy.viewmodel.productlist.ProductListViewModel.Event.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Ürün Listesi ekranı için ViewModel
 * Hibrit yaklaşım kullanır:
 * - Temel UI durumu ProductListUiState içinde tutulur (BaseViewModel'den genişletilir)
 * - PagingData ayrı bir Flow olarak tutulur (UiState içine konamaz)
 * - RowStates performans için ayrı bir StateFlow olarak tutulur
 */

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ProductListViewModel(private val productRepository: IProductRepository,
                           private val documentSession: IDocumentSession,
                           private val validator: ProductListValidator
): BaseViewModel<ProductListUiState, ProductListViewModel.Event>(ProductListUiState()) {
    //region Field
    private lateinit var documentManager: IDocumentManager

    // Filter state with debounce
    private val _filterState = MutableStateFlow(ProductFilterState())

    // Row states (separate from UiState for performance)
    private val _rowStates = MutableStateFlow<Map<String, ProductRowState>>(emptyMap())
    val rowStates: StateFlow<Map<String, ProductRowState>> = _rowStates.asStateFlow()

    // Navigation events
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()
    //endregion

    //region Properties
    /**
     * Ürünler için Paging akışı (Cash App Paging ile KMP uyumlu)
     * Filtre değişiminde 500ms debounce ile otomatik yenilenir
     */

    val products: Flow<PagingData<Product>> = _filterState
        .debounce(500) // Debounce search input
        .flatMapLatest { filter ->
            Pager(
                config = PagingConfig(
                    pageSize = 50,
                    prefetchDistance = 15,
                    enablePlaceholders = false
                )
            ) {
                ProductPagingSource(productRepository, filter)
            }.flow
        }
        .cachedIn(scope)
    //endregion

    //region Constructor
    init {
        loadAvailableFilters()
        documentManager = documentSession.current()
    }
    //endregion

    //region Public Method
    /**
     * Belge oturumunu başlat
     * Bu ekran ilk açıldığında çağrılmalıdır
     */
    fun startDocument() {
        // Load existing lines from document into row states
        scope.launch {
            documentManager.lines.collect { lines ->
                lines.forEach { line ->
                    updateRowState(line.productId) { state ->
                        state.copy(
                            isInDocument = true,
                            documentQuantity = line.quantity,
                            documentUnitId = line.unitId,
                            documentUnitName = line.unitName
                        )
                    }
                }
            }
        }
    }

    //region ========== FILTER ACTIONS ==========
    fun onSearchQueryChanged(query: String) {
        val newFilterState = state.value.filterState.copy(searchQuery = query)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState // Update debounced flow
    }

    fun onBrandsChanged(brands: Set<String>) {
        val newFilterState = state.value.filterState.copy(brands = brands)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
    }

    fun onCategoriesChanged(categories: Set<String>) {
        val newFilterState = state.value.filterState.copy(categories = categories)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
    }

    fun onColorsChanged(colors: Set<String>) {
        val newFilterState = state.value.filterState.copy(colors = colors)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
    }

    fun onPriceRangeChanged(range: PriceRange?) {
        val newFilterState = state.value.filterState.copy(priceRange = range)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
    }

    fun onTagsChanged(tags: Set<String>) {
        val newFilterState = state.value.filterState.copy(tags = tags)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
    }

    fun clearFilters() {
        val emptyFilter = ProductFilterState()
        updateState { it.copy(filterState = emptyFilter) }
        _filterState.value = emptyFilter
    }
    //endregion ========== FILTER ACTIONS ==========

    //region ========== PRODUCT ROW ACTIONS ==========

    /**
     * Bir ürün için satır durumunu başlat
     * Ürün ilk kez listede göründüğünde çağrılır
     */
    fun initializeRowState(product: Product) {
        if (_rowStates.value.containsKey(product.id)) return

        _rowStates.update { states ->
            states + (product.id to ProductRowState(
                productId = product.id,
                availableUnits = product.units,
                currentUnitIndex = product.units.indexOfFirst { it.isBaseUnit }
                    .takeIf { it >= 0 } ?: 0
            ))
        }
    }

    /**
     * Bir sonraki birime geç (Adet → Koli → Palet → Adet)
     */
    fun onUnitCycleClicked(product: Product) {
        scope.launch {
            val state = _rowStates.value[product.id] ?: return@launch
            val newIndex = (state.currentUnitIndex + 1) % product.units.size
            val newUnit = product.units[newIndex]

            val validationStatus = if (state.quantityText.isNotEmpty()) {
                validator.validateQuantity(state.quantityText, newUnit, product)
            } else {
                ValidationStatus.Empty
            }

            updateRowState(product.id) {
                it.copy(
                    currentUnitIndex = newIndex,
                    validationStatus = validationStatus
                )
            }
        }
    }

    /**
     * Miktar metni değişimini yönetir
     */
    fun onQuantityChanged(product: Product, text: String) {
        if (text.isNotEmpty() && !text.matches(Regex("^\\d*\\.?\\d*$"))) {
            return
        }

        scope.launch {
            val state = _rowStates.value[product.id] ?: return@launch
            val unit = state.currentUnit ?: return@launch

            val validationStatus = validator.validateQuantity(text, unit, product)

            updateRowState(product.id) {
                it.copy(
                    quantityText = text,
                    validationStatus = validationStatus
                )
            }
        }
    }

    /**
     * İndirim diyalogunu aç
     */
    fun onDiscountButtonClicked(product: Product) {
        val state = _rowStates.value[product.id] ?: return
        val unit = state.currentUnit ?: return

        if (!state.isValidQuantity) {
            sendEvent(Event.ShowError("Önce miktar girmelisiniz"))
            return
        }

        scope.launch {
            _navigationEvents.emit(
                NavigationEvent.OpenDiscountDialog(
                    productId = product.id,
                    product = product,
                    currentUnit = unit,
                    quantity = state.quantityText.toBigDecimal(),
                    existingDiscounts = state.discountSlots
                )
            )
        }
    }

    /**
     * Diyalogdan gelen indirimleri uygula
     */
    fun onDiscountsApplied(productId: String, discounts: List<DiscountSlotEntry>) {
        updateRowState(productId) { state ->
            state.copy(
                hasDiscount = discounts.any { it.value.isNotEmpty() },
                discountSlots = discounts
            )
        }
    }

    /**
     * Ürünü belgeye ekle
     */
    fun onAddToDocument(product: Product) {
        val state = _rowStates.value[product.id] ?: return

        // Validation
        if (!state.canAddToDocument) {
            sendEvent(Event.ShowError("Geçerli bir miktar girin"))
            return
        }

        val unit = state.currentUnit!!
        val quantity = state.quantityText.toBigDecimal()

        scope.launch {
            try {
                // Add line to document
                val result = documentManager.addLine(
                    product = product,
                    unit = unit,
                    quantity = quantity
                )

                when (result) {
                    is AddLineResult.Success -> {
                        val lineId = result.line.id

                        // Apply discounts if any
                        state.discountSlots
                            .filter { it.value.isNotEmpty() }
                            .forEach { slot ->
                                val discountValue = slot.value.toBigDecimal()
                                documentManager.applyManualDiscount(
                                    lineId = lineId,
                                    slotNumber = slot.slotNumber,
                                    type = when (slot.type) {
                                        DiscountType.PERCENTAGE ->
                                            DiscountType.PERCENTAGE
                                        DiscountType.FIXED_AMOUNT ->
                                            DiscountType.FIXED_AMOUNT
                                    },
                                    value = discountValue
                                )
                            }

                        // Update state - keep quantity and discounts for next addition
                        updateRowState(product.id) {
                            it.copy(
                                isInDocument = true,
                                documentQuantity = it.documentQuantity + quantity,
                                documentUnitId = unit.id,
                                documentUnitName = unit.unitName
                            )
                        }

                        sendEvent(ShowSuccess("Ürün sepete eklendi"))
                    }

                    is AddLineResult.NeedsConfirmation -> {
                        //TODO BURAYA BAKACAGIZ
                        //sendEvent(Event.ShowError(result.message))
                    }

                    is AddLineResult.Blocked -> {
                        sendEvent(ShowError(result.error.message))
                    }

                    AddLineResult.NotFound -> {
                        sendEvent(ShowSuccess("Ürün bulunamadı"))
                    }
                }
            } catch (e: Exception) {
                sendEvent(Event.ShowError("Hata: ${e.message}"))
            }
        }
    }

    /**
     * Sepete/sonraki ekrana ilerle
     */
    fun onNextClicked() {
        scope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateToCart)
        }
    }

    /**
     * Bir ürün için row state'i getir
     */
    fun getRowState(productId: String): ProductRowState? {
        return _rowStates.value[productId]
    }

    //endregion ========== PRODUCT ROW ACTIONS ==========

    //endregion

    //region Private Method
    /**
     * Belirli ürün satırı durumunu güncelle
     */
    private fun updateRowState(productId: String, update: (ProductRowState) -> ProductRowState) {
        _rowStates.update { states ->
            val currentState = states[productId] ?: return@update states
            states + (productId to update(currentState))
        }
    }

    /**
     * Mevcut filtre seçeneklerini yükle (benzersiz değerler)
     */
    private fun loadAvailableFilters() {
        scope.launch {
            try {
                val filters = productRepository.getAvailableFilters()
                updateState { it.copy(availableFilters = filters) }
            } catch (e: Exception) {
                sendEvent(Event.ShowError("Filtreler yüklenemedi: ${e.message}"))
            }
        }
    }
    //endregion

    //region Events
    sealed class Event {
        data class ShowError(val message: String) : Event()
        data class ShowSuccess(val message: String) : Event()
    }

    /**
     * Navigasyon Olayları (ekran geçişleri)
     */
    sealed class NavigationEvent {
        data class OpenDiscountDialog(
            val productId: String,
            val product: Product,
            val currentUnit: ProductUnit,
            val quantity: BigDecimal,
            val existingDiscounts: List<DiscountSlotEntry>
        ) : NavigationEvent()

        object NavigateToCart : NavigationEvent()
    }
    //endregion Events
}