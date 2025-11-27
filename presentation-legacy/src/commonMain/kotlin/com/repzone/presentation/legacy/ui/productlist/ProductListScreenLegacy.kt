package com.repzone.presentation.legacy.ui.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.repzone.core.model.StringResource
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.floatactionbutton.SmartFabScaffold
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.document.model.Product
import com.repzone.domain.model.product.ProductRowState
import com.repzone.presentation.legacy.model.enum.ProductSortOption
import com.repzone.presentation.legacy.ui.productlist.component.DiscountDialogLegacy
import com.repzone.presentation.legacy.ui.productlist.component.ProductFilterBottomSheet
import com.repzone.presentation.legacy.ui.productlist.component.ProductListFilterBar
import com.repzone.presentation.legacy.ui.productlist.component.ProductRow
import com.repzone.presentation.legacy.viewmodel.productlist.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreenLegacy(onDissmiss: () -> Unit) = ViewModelHost<ProductListViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    // 1. UiState
    val uiState by viewModel.state.collectAsState()
    // 2. PagingData (ayrı Flow)
    val products = viewModel.products.collectAsLazyPagingItems()
    // 3. RowStates (ayrı StateFlow - performance)
    val rowStates by viewModel.rowStates.collectAsState()
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Filter BottomSheet state
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf(ProductSortOption.NAME_ASC) }

    // Initialize document
    LaunchedEffect(Unit) {
        viewModel.startDocument()
    }

    // 4. Handle UI events (BaseViewModel)
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

    // 5. Handle navigation events
    var showDiscountDialog by remember { mutableStateOf<ProductListViewModel.NavigationEvent.OpenDiscountDialog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is ProductListViewModel.NavigationEvent.OpenDiscountDialog -> {
                    showDiscountDialog = event
                }
                ProductListViewModel.NavigationEvent.NavigateToCart -> {}
            }
        }
    }

    SmartFabScaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        fabAction = FabAction.Single(icon = Icons.Default.SkipNext, "Satış Siparişi Oluştur"),
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
                title = StringResource.PRODUCTS.fromResource(),
                subtitle = "Satış Siparişi - Salih Müşterisi"
            )

            // Initial loading (UiFrame)
            if (uiState.uiFrame.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Filter bar (Search + Filter Button)
                ProductListFilterBar(
                    filterState = uiState.filterState,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onFilterButtonClick = { showFilterSheet = true },
                    modifier = Modifier,
                    themeManager = themeManager
                )

                HorizontalDivider()

                // Product list with paging
                ProductList(
                    products = products,
                    rowStates = rowStates,
                    hasDiscountPermission = true,
                    viewModel = viewModel,
                    themeManager = themeManager
                )
            }
        }
    }

    // Filter Bottom Sheet
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

    // Discount dialog
    showDiscountDialog?.let { dialogEvent ->
        DiscountDialogLegacy(
            product = dialogEvent.product,
            unit = dialogEvent.currentUnit,
            quantity = dialogEvent.quantity,
            existingDiscounts = dialogEvent.existingDiscounts,
            slotConfigs = listOf(),
            onApply = { discounts ->
                viewModel.onDiscountsApplied(dialogEvent.productId, discounts)
                showDiscountDialog = null
            },
            onDismiss = {
                showDiscountDialog = null
            }
        )
    }
}

private val ProductRowModifier = Modifier.fillMaxWidth()

@Composable
private fun ProductList(
    products: LazyPagingItems<Product>,
    rowStates: Map<String, ProductRowState>,
    hasDiscountPermission: Boolean,
    viewModel: ProductListViewModel,
    themeManager: ThemeManager
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Refresh loading state
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
            key = { index ->
                products.peek(index)?.id ?: "item_$index"
            }
        ) { index ->
            val product = products[index] ?: return@items
            ProductRowItem(
                product = product,
                rowState = rowStates[product.id],
                hasDiscountPermission = hasDiscountPermission,
                viewModel = viewModel,
                themeManager = themeManager
            )
        }

        // Append loading
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

        // Empty state
        if (products.loadState.refresh is LoadStateNotLoading && products.itemCount == 0) {
            item(key = "empty_state") {
                EmptyState(message = "Ürün bulunamadı")
            }
        }
    }
}

/**
 * Ayrı composable - her row kendi recomposition scope'una sahip
 * Bu sayede bir row değiştiğinde sadece o row recompose olur
 */
@Composable
private fun ProductRowItem(
    product: Product,
    rowState: ProductRowState?,
    hasDiscountPermission: Boolean,
    viewModel: ProductListViewModel,
    themeManager: ThemeManager
) {
    LaunchedEffect(product.id) {
        viewModel.initializeRowState(product)
    }

    if (rowState == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
        return
    }

    val onUnitCycle = remember(product.id) {
        { viewModel.onUnitCycleClicked(product) }
    }

    val onQuantityChanged = remember(product.id) {
        { text: String -> viewModel.onQuantityChanged(product, text) }
    }

    val onDiscountClick = remember(product.id) {
        { viewModel.onDiscountButtonClicked(product) }
    }

    ProductRow(
        product = product,
        state = rowState,
        hasDiscountPermission = hasDiscountPermission,
        onUnitCycle = onUnitCycle,
        onQuantityChanged = onQuantityChanged,
        onDiscountClick = onDiscountClick,
        modifier = ProductRowModifier,
        themeManager = themeManager
    )

    HorizontalDivider()
}

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