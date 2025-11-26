package com.repzone.presentation.legacy.ui.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.domain.document.model.Product
import com.repzone.domain.model.product.ProductRowState
import com.repzone.presentation.legacy.ui.productlist.component.DiscountDialogLegacy
import com.repzone.presentation.legacy.ui.productlist.component.ProductListFilterBar
import com.repzone.presentation.legacy.ui.productlist.component.ProductRow
import com.repzone.presentation.legacy.viewmodel.productlist.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreenLegacy(onDissmiss: () -> Unit) = ViewModelHost<ProductListViewModel>(){ viewModel ->
    // 1. UiState
    val uiState  by viewModel.state.collectAsState()
    // 2. PagingData (ayrı Flow)
    val products = viewModel.products.collectAsLazyPagingItems()
    // 3. RowStates (ayrı StateFlow - performance)
    val rowStates by viewModel.rowStates.collectAsState()
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

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
                ProductListViewModel.NavigationEvent.NavigateToCart -> {

                }
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text("Ürün Listesi") },
                actions = {
                    TextButton(onClick = { viewModel.onNextClicked() }) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sepete Git")
                            Icon(Icons.Default.ArrowForward, null)
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onNextClicked() },
                icon = { Icon(Icons.Default.ArrowForward, "İleri") },
                text = { Text("İleri") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Initial loading (UiFrame)
            if (uiState.uiFrame.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Filter bar
                ProductListFilterBar(
                    filterState = uiState.filterState,
                    availableFilters = uiState.availableFilters,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onBrandsChanged = viewModel::onBrandsChanged,
                    onCategoriesChanged = viewModel::onCategoriesChanged,
                    onColorsChanged = viewModel::onColorsChanged,
                    onTagsChanged = viewModel::onTagsChanged,
                    onPriceRangeChanged = viewModel::onPriceRangeChanged,
                    onClearFilters = viewModel::clearFilters,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider()

                // Product list with paging
                ProductList(
                    products = products,
                    rowStates = rowStates,
                    hasDiscountPermission = true,
                    viewModel = viewModel
                )
            }
        }
    }

    // Discount dialog
    showDiscountDialog?.let { dialogEvent ->
        DiscountDialogLegacy(
            product = dialogEvent.product,
            unit = dialogEvent.currentUnit,
            quantity = dialogEvent.quantity,
            existingDiscounts = dialogEvent.existingDiscounts,
            slotConfigs = listOf(), // TODO: Get from settings
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

@Composable
private fun ProductList(
    products: LazyPagingItems<Product>,
    rowStates: Map<String, ProductRowState>,
    hasDiscountPermission: Boolean,
    viewModel: ProductListViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Refresh loading state
        when (products.loadState.refresh) {
            is LoadStateLoading -> {
                item {
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
                item {
                    ErrorState(
                        message = "Ürünler yüklenirken hata: ${error.message}",
                        onRetry = { products.retry() }
                    )
                }
            }
            else -> Unit
        }

        // Product items
        items(
            count = products.itemCount,
            key = { index -> products[index]?.id ?: index }
        ) { index ->
            val product = products[index]

            if (product != null) {
                LaunchedEffect(product.id) {
                    viewModel.initializeRowState(product)
                }

                val rowState = rowStates[product.id]

                if (rowState != null) {
                    ProductRow(
                        product = product,
                        state = rowState,
                        hasDiscountPermission = hasDiscountPermission,
                        onUnitCycle = { viewModel.onUnitCycleClicked(product) },
                        onQuantityChanged = { text -> viewModel.onQuantityChanged(product, text) },
                        onDiscountClick = { viewModel.onDiscountButtonClicked(product) },
                        onAddClick = { viewModel.onAddToDocument(product) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // Append loading (scroll için daha fazla yükleme)
        when (products.loadState.append) {
            is LoadStateLoading -> {
                item {
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
                item {
                    TextButton(
                        onClick = { products.retry() },
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
            item {
                EmptyState(message = "Ürün bulunamadı")
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
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