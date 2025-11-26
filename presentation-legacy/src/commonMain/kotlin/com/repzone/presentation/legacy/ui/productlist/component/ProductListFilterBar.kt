package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.model.product.ProductFilters

@Composable
fun ProductListFilterBar(
    filterState: ProductFilterState,
    availableFilters: ProductFilters?,
    onSearchQueryChanged: (String) -> Unit,
    onBrandsChanged: (Set<String>) -> Unit,
    onCategoriesChanged: (Set<String>) -> Unit,
    onColorsChanged: (Set<String>) -> Unit,
    onTagsChanged: (Set<String>) -> Unit,
    onPriceRangeChanged: (PriceRange?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search bar
        SearchBar(
            query = filterState.searchQuery,
            onQueryChange = onSearchQueryChanged
        )
        
        // Filter chips
        if (availableFilters != null) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // Brand filter
                if (availableFilters.brands.isNotEmpty()) {
                    item {
                        MultiSelectFilterChip(
                            label = "Marka",
                            count = filterState.brands.size,
                            isSelected = filterState.brands.isNotEmpty(),
                            onClick = { 
                                // Show dialog for multi-select
                            }
                        )
                    }
                }
                
                // Category filter
                if (availableFilters.categories.isNotEmpty()) {
                    item {
                        MultiSelectFilterChip(
                            label = "Kategori",
                            count = filterState.categories.size,
                            isSelected = filterState.categories.isNotEmpty(),
                            onClick = { }
                        )
                    }
                }
                
                // Color filter
                if (availableFilters.colors.isNotEmpty()) {
                    item {
                        MultiSelectFilterChip(
                            label = "Renk",
                            count = filterState.colors.size,
                            isSelected = filterState.colors.isNotEmpty(),
                            onClick = { }
                        )
                    }
                }
                
                // Tag filter
                if (availableFilters.tags.isNotEmpty()) {
                    item {
                        MultiSelectFilterChip(
                            label = "Etiket",
                            count = filterState.tags.size,
                            isSelected = filterState.tags.isNotEmpty(),
                            onClick = { }
                        )
                    }
                }
                
                // Price range filter
                item {
                    FilterChip(
                        selected = filterState.priceRange != null,
                        onClick = { },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Fiyat")
                            }
                        },
                        leadingIcon = if (filterState.priceRange != null) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null
                    )
                }
                
                // Clear filters
                if (filterState.hasActiveFilters) {
                    item {
                        AssistChip(
                            onClick = onClearFilters,
                            label = { Text("Temizle") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
        
        // Active filter summary
        if (filterState.hasActiveFilters) {
            Text(
                text = "${filterState.activeFilterCount} filtre aktif",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Ürün ara...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ara"
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Temizle"
                    )
                }
            }
        } else null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { /* Hide keyboard */ }
        )
    )
}

@Composable
private fun MultiSelectFilterChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        modifier = modifier,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label)
                if (count > 0) {
                    Badge {
                        Text(count.toString())
                    }
                }
            }
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null
    )
}

/**
 * Multi-select dialog for filters
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDialog(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onConfirm: (Set<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedOptions) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(options) { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = option in currentSelection,
                            onCheckedChange = { checked ->
                                currentSelection = if (checked) {
                                    currentSelection + option
                                } else {
                                    currentSelection - option
                                }
                            }
                        )
                        Text(option)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentSelection) }) {
                Text("Tamam")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

/**
 * Price range dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeDialog(
    currentRange: PriceRange?,
    minPrice: BigDecimal?,
    maxPrice: BigDecimal?,
    onConfirm: (PriceRange?) -> Unit,
    onDismiss: () -> Unit
) {
    var minValue by remember { mutableStateOf(currentRange?.min?.toPlainString() ?: "") }
    var maxValue by remember { mutableStateOf(currentRange?.max?.toPlainString() ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Fiyat Aralığı") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minValue,
                    onValueChange = { minValue = it },
                    label = { Text("Min Fiyat") },
                    suffix = { Text("₺") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                
                OutlinedTextField(
                    value = maxValue,
                    onValueChange = { maxValue = it },
                    label = { Text("Max Fiyat") },
                    suffix = { Text("₺") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                
                if (minPrice != null && maxPrice != null) {
                    Text(
                        text = "Fiyat aralığı: ${minPrice.toPlainString()} - ${maxPrice.toPlainString()} ₺",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val min = minValue.toBigDecimalOrNull()
                    val max = maxValue.toBigDecimalOrNull()
                    
                    if (min != null || max != null) {
                        onConfirm(PriceRange(min, max))
                    } else {
                        onConfirm(null)
                    }
                }
            ) {
                Text("Uygula")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
