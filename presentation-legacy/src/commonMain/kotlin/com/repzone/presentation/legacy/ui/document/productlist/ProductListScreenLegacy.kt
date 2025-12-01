package com.repzone.presentation.legacy.ui.document.productlist

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.model.StringResource
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.floatactionbutton.SmartFabScaffold
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.util.getDocumentNameForResource
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.model.product.ProductRowState
import com.repzone.presentation.legacy.model.enum.ProductSortOption
import com.repzone.presentation.legacy.ui.document.productlist.component.DiscountDialogLegacy
import com.repzone.presentation.legacy.ui.document.productlist.component.ProductFilterBottomSheet
import com.repzone.presentation.legacy.ui.document.productlist.component.ProductListFilterBar
import com.repzone.presentation.legacy.ui.document.productlist.component.ProductRow
import com.repzone.presentation.legacy.viewmodel.document.productlist.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreenLegacy(onDissmiss: () -> Unit, onNavigateDocumentSettings: () -> Unit) = ViewModelHost<ProductListViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val backgroundColor = remember(themeManager) {
        themeManager.getCurrentColorScheme().colorPalet.neutral95
    }

    val uiState by viewModel.state.collectAsState()
    val products = viewModel.products.collectAsLazyPagingItems()
    val rowStates by viewModel.rowStates.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val entryCount = viewModel.entryCount.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf(ProductSortOption.NAME_ASC) }

    LaunchedEffect(Unit) {
        viewModel.startDocument()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ProductListViewModel.Event.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is ProductListViewModel.Event.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    var showDiscountDialog by remember { mutableStateOf<ProductListViewModel.NavigationEvent.OpenDiscountDialog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is ProductListViewModel.NavigationEvent.OpenDiscountDialog -> {
                    showDiscountDialog = event
                }
                ProductListViewModel.NavigationEvent.NavigateToCart -> {
                    onNavigateDocumentSettings()
                }
            }
        }
    }

    SmartFabScaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        fabAction = FabAction.Single(
            icon = Icons.Default.SkipNext,
            ".",
            badgeCount = entryCount.value
        ),
        onFabClick = {
            if(!uiState.onFabClickedProgress){
                viewModel.onNextClicked()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            RepzoneTopAppBar(
                modifier = Modifier.padding(0.dp),
                themeManager = themeManager,
                leftIconType = TopBarLeftIcon.Back(onClick = onDissmiss),
                rightIcons = listOf(
                    TopBarAction(Icons.Default.Timer, "Timer", Color.White, {}),
                    TopBarAction(Icons.Default.Map, "Map", Color.White, {}),
                ),
                title = viewModel.getDocumentName().getDocumentNameForResource(),
                subtitle = viewModel.getDocumentSubTitle()
            )

            if(uiState.onFabClickedProgress){
                CircularProgressIndicator()
            }

            if (uiState.uiFrame.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ProductListFilterBar(
                    filterState = uiState.filterState,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onFilterButtonClick = { showFilterSheet = true },
                    modifier = Modifier,
                    themeManager = themeManager
                )

                HorizontalDivider()

                ProductList(
                    products = products,
                    rowStates = rowStates,
                    hasDiscountPermission = true,
                    viewModel = viewModel,
                    backgroundColor = backgroundColor
                )
            }
        }
    }

    uiState.availableFilters?.let { filters ->
        ProductFilterBottomSheet(
            showBottomSheet = showFilterSheet,
            onDismiss = { showFilterSheet = false },
            filterState = uiState.filterState,
            availableFilters = filters,
            selectedSort = selectedSort,
            onApplyFilters = { brands, categories, colors, tags, priceRange, sort ->
                selectedSort = sort
                viewModel.onBrandsChanged(brands)
                viewModel.onCategoriesChanged(categories)
                viewModel.onColorsChanged(colors)
                viewModel.onTagsChanged(tags)
                viewModel.onPriceRangeChanged(priceRange)
                showFilterSheet = false
            },
            onClearFilters = {
                selectedSort = ProductSortOption.NAME_ASC
                viewModel.clearFilters()
                showFilterSheet = false
            }
        )
    }

    showDiscountDialog?.let { dialogEvent ->
        DiscountDialogLegacy(
            product = dialogEvent.product,
            unit = dialogEvent.currentUnit,
            quantity = dialogEvent.quantity,
            existingDiscounts = dialogEvent.existingDiscounts,
            slotConfigs = listOf(
                DiscountSlotConfig(
                    slotNumber = 1,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 2,
                    name = "İndirim Iskontosu 2",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(40)
                ),
                DiscountSlotConfig(
                    slotNumber = 3,
                    name = "İndirim Iskontosu 3 Deneme",
                    allowManualEntry = false,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 4,
                    name = "İndirim Iskontosu 4",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(14)
                ),
                DiscountSlotConfig(
                    slotNumber = 5,
                    name = "İndirim Iskontosu Tutar",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = null,
                    slotType = DiscountType.FIXED_AMOUNT


                ),
                DiscountSlotConfig(
                    slotNumber = 6,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 7,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 8,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                )
            ),
            onApply = { discounts ->
                viewModel.onDiscountsApplied(dialogEvent.productId, discounts)
                showDiscountDialog = null
            },
            onDismiss = {
                showDiscountDialog = null
            },
            themeManager = themeManager
        )
    }
}

@Composable
private fun ProductList(
    products: LazyPagingItems<ProductInformationModel>,
    rowStates: Map<Int, ProductRowState>,
    hasDiscountPermission: Boolean,
    viewModel: ProductListViewModel,
    backgroundColor: Color
) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 88.dp
        ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        when (products.loadState.refresh) {
            is LoadStateLoading -> {
                item(key = "refresh_loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadStateError -> {
                val error = (products.loadState.refresh as LoadStateError).error
                item(key = "refresh_error") {
                    ErrorState(
                        message = "Ürünler yüklenirken hata: ${error.message}",
                        onRetry = products::retry
                    )
                }
            }
            else -> Unit
        }

        items(
            count = products.itemCount,
            key = { index -> products.peek(index)?.id ?: "item_$index" }
        ) { index ->
            val product = products[index] ?: return@items
            val rowState = rowStates[product.id]

            ProductRowOptimized(
                product = product,
                rowState = rowState,
                hasDiscountPermission = hasDiscountPermission,
                viewModel = viewModel,
                backgroundColor = backgroundColor,
                focusManager = focusManager
            )
        }

        when (products.loadState.append) {
            is LoadStateLoading -> {
                item(key = "append_loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }
            is LoadStateError -> {
                item(key = "append_error") {
                    TextButton(
                        onClick = products::retry,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Daha fazla yükle")
                    }
                }
            }
            else -> Unit
        }

        if (products.loadState.refresh is LoadStateNotLoading && products.itemCount == 0) {
            item(key = "empty_state") {
                EmptyState(message = "Ürün bulunamadı")
            }
        }
    }
}
@Composable
private fun ProductRowOptimized(
    product: ProductInformationModel,
    rowState: ProductRowState?,
    hasDiscountPermission: Boolean,
    viewModel: ProductListViewModel,
    backgroundColor: Color,
    focusManager: FocusManager
) {
    val displayState = rowState ?: viewModel.getDisplayState(product)

    val callbacks = remember(product.id) {
        ProductRowCallbacks(
            onUnitCycle = {
                focusManager.clearFocus()
                viewModel.onUnitCycleClicked(product) },
            onQuantityChanged = { text -> viewModel.onQuantityChanged(product, text) },
            onDiscountClick = { viewModel.onDiscountButtonClicked(product) }
        )
    }

    ProductRow(
        product = product,
        state = displayState,
        hasDiscountPermission = hasDiscountPermission,
        onUnitCycle = callbacks.onUnitCycle,
        onQuantityChanged = callbacks.onQuantityChanged,
        onDiscountClick = callbacks.onDiscountClick,
        backgroundColor = backgroundColor
    )

    HorizontalDivider()
}
private data class ProductRowCallbacks(
    val onUnitCycle: () -> Unit,
    val onQuantityChanged: (String) -> Unit,
    val onDiscountClick: () -> Unit
)

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Button(onClick = onRetry) {
            Text("Tekrar Dene")
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}