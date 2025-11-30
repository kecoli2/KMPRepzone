package com.repzone.presentation.legacy.viewmodel.document.productlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.data.repository.product.ProductPagingSource
import com.repzone.domain.model.product.ProductRowState
import com.repzone.domain.model.product.UnitEntry
import com.repzone.domain.document.base.AddLineResult
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductListValidator
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.ValidationStatus
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.repository.IProductRepository
import com.repzone.presentation.legacy.viewmodel.document.productlist.ProductListViewModel.Event.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.iterator

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ProductListViewModel(
    private val productRepository: IProductRepository,
    private val documentSession: IDocumentSession,
    private val validator: ProductListValidator
): BaseViewModel<ProductListUiState, ProductListViewModel.Event>(ProductListUiState()) {

    //region Field
    private var documentManager: IDocumentManager
    private val _filterState = MutableStateFlow(ProductFilterState())
    private val _rowStates = MutableStateFlow<Map<Int, ProductRowState>>(emptyMap())
    val rowStates: StateFlow<Map<Int, ProductRowState>> = _rowStates.asStateFlow()
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()
    //endregion

    //region Properties
    val products: Flow<PagingData<ProductInformationModel>> = _filterState
        .debounce(500)
        .flatMapLatest { filter ->
            Pager(
                config = PagingConfig(
                    pageSize = 50,
                    prefetchDistance = 15,
                    enablePlaceholders = false
                )
            ) {
                ProductPagingSource(productRepository, filter, documentManager.getProductQueryString(), documentManager.getProductUnitMap())
            }.flow
        }
        .cachedIn(scope)

    /**
     * Giriş yapılmış ürün sayısı (FAB badge için)
     */
    val entryCount: StateFlow<Int> = _rowStates
        .map { states -> states.values.count { it.hasAnyEntry } }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), 0)
    //endregion

    //region Constructor
    init {
        documentManager = documentSession.current()
        loadAvailableFilters()
    }
    //endregion

    //region Public Method

    fun startDocument() {
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

    fun getDocumentSubTitle(): String {
        return documentManager.getDocumentMapModel().description + "- ${documentManager.getCustomer().name}"
    }

    override fun onDispose() {
        documentSession.clear()
        documentManager.clear()
    }

    //endregion Public Method

    //region ========== FILTER ACTIONS ==========

    fun onSearchQueryChanged(query: String) {
        val newFilterState = state.value.filterState.copy(searchQuery = query)
        updateState { it.copy(filterState = newFilterState) }
        _filterState.value = newFilterState
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

    //endregion

    //region ========== PRODUCT ROW ACTIONS ==========

    /**
     * Display state (miktar girilmemiş ürünler için default state döner)
     */
    fun getDisplayState(product: ProductInformationModel): ProductRowState {
        return _rowStates.value[product.id] ?: ProductRowState(
            productId = product.id,
            availableUnits = product.units,
            currentUnitIndex = product.units.indexOfFirst { it.isBaseUnit }
                .takeIf { it >= 0 } ?: 0
        )
    }

    /**
     * Miktar değiştiğinde çağrılır
     */
    fun onQuantityChanged(product: ProductInformationModel, text: String) {
        if (text.isNotEmpty() && !text.matches(Regex("^\\d*\\.?\\d*$"))) {
            return
        }

        scope.launch {
            val existingState = _rowStates.value[product.id]

            // State yoksa ve text boşsa bir şey yapma
            if (existingState == null && text.isEmpty()) {
                return@launch
            }

            // State yoksa oluştur
            val state = existingState ?: ProductRowState(
                productId = product.id,
                availableUnits = product.units,
                currentUnitIndex = product.units.indexOfFirst { it.isBaseUnit }
                    .takeIf { it >= 0 } ?: 0
            )

            val unit = state.currentUnit ?: return@launch

            val validationStatus = if (text.isNotEmpty()) {
                val reservedBaseQuantity = calculateReservedBaseQuantity(state)
                validator.validateQuantity(text, unit, product, reservedBaseQuantity)
            } else {
                ValidationStatus.Empty
            }

            val newState = state.copy(
                quantityText = text,
                validationStatus = validationStatus
            )

            // Text boş ve entry yoksa ve belgede değilse state'i sil
            if (text.isEmpty() && newState.unitEntries.isEmpty() && !newState.isInDocument) {
                _rowStates.update { it - product.id }
            } else {
                _rowStates.update { it + (product.id to newState) }
            }
        }
    }

    /**
     * Birim değiştiğinde çağrılır
     * Mevcut miktar varsa kaydedilir, yeni birime geçilir
     */
    fun onUnitCycleClicked(product: ProductInformationModel) {
        scope.launch {
            val existingState = _rowStates.value[product.id]

            // State yoksa oluştur
            val state = existingState ?: ProductRowState(
                productId = product.id,
                availableUnits = product.units,
                currentUnitIndex = product.units.indexOfFirst { it.isBaseUnit }
                    .takeIf { it >= 0 } ?: 0
            )

            val currentUnit = state.currentUnit ?: return@launch

            // Mevcut birimde miktar varsa VE validation error değilse unitEntries'e kaydet
            var updatedEntries = state.unitEntries
            if (state.isValidQuantity && state.validationStatus !is ValidationStatus.Error) {
                val quantity = state.quantityText.toBigDecimal()
                val existingEntry = updatedEntries[currentUnit.unitId]

                // Aynı birimde önceki giriş varsa topla
                val newQuantity = if (existingEntry != null) {
                    existingEntry.quantity + quantity
                } else {
                    quantity
                }

                updatedEntries = updatedEntries + (currentUnit.unitId to UnitEntry(
                    unitId = currentUnit.unitId,
                    unitName = currentUnit.unitName,
                    quantity = newQuantity,
                    hasDiscount = state.hasDiscount,
                    discountSlots = state.discountSlots
                ))
            }

            // Yeni birime geç
            val newIndex = (state.currentUnitIndex + 1) % product.units.size
            val newUnit = product.units[newIndex]

            // Yeni birimde önceden giriş var mı?
            val existingNewEntry = updatedEntries[newUnit.unitId]
            val newQuantityText = existingNewEntry?.quantity?.toPlainString() ?: ""

            // Yeni birimde entry varsa entries'den çıkar (düzenleme moduna alıyoruz)
            if (existingNewEntry != null) {
                updatedEntries = updatedEntries - newUnit.unitId
            }

            // reservedBaseQuantity hesapla
            val reservedBaseQuantity = calculateReservedBaseQuantity(state.copy(unitEntries = updatedEntries))

            val validationStatus = if (newQuantityText.isNotEmpty()) {
                validator.validateQuantity(newQuantityText, newUnit, product, reservedBaseQuantity)
            } else {
                ValidationStatus.Empty
            }

            val newState = state.copy(
                currentUnitIndex = newIndex,
                quantityText = newQuantityText,
                validationStatus = validationStatus,
                unitEntries = updatedEntries,
                hasDiscount = existingNewEntry?.hasDiscount ?: false,
                discountSlots = existingNewEntry?.discountSlots ?: emptyList()
            )

            _rowStates.update { it + (product.id to newState) }
        }
    }

    /**
     * İndirim diyalogunu aç
     */
    fun onDiscountButtonClicked(product: ProductInformationModel) {
        val state = _rowStates.value[product.id] ?: return
        val unit = state.currentUnit ?: return

        if (!state.isValidQuantity) {
            sendEvent(ShowError("Önce miktar girmelisiniz"))
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
    fun onDiscountsApplied(productId: Int, discounts: List<DiscountSlotEntry>) {
        updateRowState(productId) { state ->
            state.copy(
                hasDiscount = discounts.any { it.value.isNotEmpty() },
                discountSlots = discounts
            )
        }
    }

    /**
     * FAB'a basıldığında - tüm girişleri belgeye ekler
     */
    fun onNextClicked() {
        scope.launch {
            val statesWithEntries = _rowStates.value.filter { (_, state) -> state.hasAnyEntry }

            if (statesWithEntries.isEmpty()) {
                sendEvent(ShowError("Eklenecek ürün yok"))
                return@launch
            }

            var successCount = 0
            var errorMessage: String? = null

            for ((productId, state) in statesWithEntries) {
                val product = productRepository.getProductById(documentManager.getProductQueryString(), productId, state.availableUnits)

                try {
                    // 1. Kaydedilmiş entry'leri ekle
                    for ((unitId, entry) in state.unitEntries) {
                        val unit = state.availableUnits.find { it.unitId == unitId } ?: continue

                        val result = documentManager.addLine(
                            product = product,
                            unit = unit,
                            quantity = entry.quantity
                        )

                        when (result) {
                            is AddLineResult.Success -> {
                                applyDiscountsToLine(result.line.id, entry.discountSlots)
                                successCount++
                            }
                            is AddLineResult.Blocked -> {
                                errorMessage = result.error.message
                            }
                            is AddLineResult.NeedsConfirmation -> { /* TODO */ }
                            AddLineResult.NotFound -> {}
                        }
                    }

                    // 2. Mevcut girişi ekle
                    if (state.isValidQuantity) {
                        val unit = state.currentUnit ?: continue
                        val quantity = state.quantityText.toBigDecimal()

                        val result = documentManager.addLine(
                            product = product,
                            unit = unit,
                            quantity = quantity
                        )

                        when (result) {
                            is AddLineResult.Success -> {
                                applyDiscountsToLine(result.line.id, state.discountSlots)
                                successCount++
                            }
                            is AddLineResult.Blocked -> {
                                errorMessage = result.error.message
                            }
                            is AddLineResult.NeedsConfirmation -> { /* TODO */ }
                            AddLineResult.NotFound -> {}
                        }
                    }

                    // 3. State'i temizle
                    updateRowState(productId) {
                        it.copy(
                            quantityText = "",
                            validationStatus = ValidationStatus.Empty,
                            unitEntries = emptyMap(),
                            hasDiscount = false,
                            discountSlots = emptyList(),
                            isInDocument = true
                        )
                    }

                } catch (e: Exception) {
                    errorMessage = e.message
                }
            }

            // Sonuç bildirimi
            if (errorMessage != null) {
                sendEvent(ShowError(errorMessage))
            } else if (successCount > 0) {
                sendEvent(ShowSuccess("$successCount satır eklendi"))
                _navigationEvents.emit(NavigationEvent.NavigateToCart)
            }
        }
    }

    //endregion

    //region Private Method

    private fun updateRowState(productId: Int, update: (ProductRowState) -> ProductRowState) {
        _rowStates.update { states ->
            val currentState = states[productId] ?: return@update states
            states + (productId to update(currentState))
        }
    }

    private suspend fun applyDiscountsToLine(lineId: String, discountSlots: List<DiscountSlotEntry>) {
        discountSlots
            .filter { it.value.isNotEmpty() }
            .forEach { slot ->
                documentManager.applyManualDiscount(
                    lineId = lineId,
                    slotNumber = slot.slotNumber,
                    type = slot.type,
                    value = slot.value.toBigDecimal()
                )
            }
    }

    private fun loadAvailableFilters() {
        scope.launch {
            try {
                val filters = documentManager.getAvailableFilters()
                updateState { it.copy(availableFilters = filters) }
            } catch (e: Exception) {
                sendEvent(ShowError("Filtreler yüklenemedi: ${e.message}"))
            }
        }
    }

    private fun calculateReservedBaseQuantity(state: ProductRowState): BigDecimal {
        return state.unitEntries.values.fold(BigDecimal.ZERO) { acc, entry ->
            val entryUnit = state.availableUnits.find { it.unitId == entry.unitId }
            val conversionFactor = entryUnit?.multiplier ?: BigDecimal.ONE
            acc + (entry.quantity * conversionFactor)
        }
    }

    //endregion

    //region Events

    sealed class Event {
        data class ShowError(val message: String) : Event()
        data class ShowSuccess(val message: String) : Event()
    }

    sealed class NavigationEvent {
        data class OpenDiscountDialog(
            val productId: Int,
            val product: ProductInformationModel,
            val currentUnit: ProductUnit,
            val quantity: BigDecimal,
            val existingDiscounts: List<DiscountSlotEntry>
        ) : NavigationEvent()

        object NavigateToCart : NavigationEvent()
    }

    //endregion
}