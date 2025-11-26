@file:OptIn(ExperimentalMaterial3Api::class)

package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.model.product.ProductFilters
import com.repzone.presentation.legacy.model.enum.ProductSortOption
import com.repzone.presentation.legacy.ui.customerlist.FilterSection
import repzonemobile.core.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilterBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    filterState: ProductFilterState,
    availableFilters: ProductFilters,
    selectedSort: ProductSortOption,
    onApplyFilters: (
        brands: Set<String>,
        categories: Set<String>,
        colors: Set<String>,
        tags: Set<String>,
        priceRange: PriceRange?,
        sort: ProductSortOption
    ) -> Unit,
    onClearFilters: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Temp states
    var tempBrands by remember(filterState.brands) { mutableStateOf(filterState.brands) }
    var tempCategories by remember(filterState.categories) { mutableStateOf(filterState.categories) }
    var tempColors by remember(filterState.colors) { mutableStateOf(filterState.colors) }
    var tempTags by remember(filterState.tags) { mutableStateOf(filterState.tags) }
    var tempMinPrice by remember(filterState.priceRange) {
        mutableStateOf(filterState.priceRange?.min?.toPlainString() ?: "")
    }
    var tempMaxPrice by remember(filterState.priceRange) {
        mutableStateOf(filterState.priceRange?.max?.toPlainString() ?: "")
    }
    var tempSort by remember(selectedSort) { mutableStateOf(selectedSort) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ürün Filtreleri",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat"
                        )
                    }
                }

                HorizontalDivider()

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Marka Section
                    if (availableFilters.brands.isNotEmpty()) {
                        FilterSection(
                            title = "Marka",
                            content = {
                                MultiSelectChipGroup(
                                    options = availableFilters.brands.toList(),
                                    selectedOptions = tempBrands,
                                    onToggle = { brand ->
                                        tempBrands = if (brand in tempBrands) {
                                            tempBrands - brand
                                        } else {
                                            tempBrands + brand
                                        }
                                    }
                                )
                            }
                        )
                    }

                    // Kategori Section
                    if (availableFilters.categories.isNotEmpty()) {
                        FilterSection(
                            title = "Kategori",
                            content = {
                                MultiSelectChipGroup(
                                    options = availableFilters.categories.toList(),
                                    selectedOptions = tempCategories,
                                    onToggle = { category ->
                                        tempCategories = if (category in tempCategories) {
                                            tempCategories - category
                                        } else {
                                            tempCategories + category
                                        }
                                    }
                                )
                            }
                        )
                    }

                    // Renk Section
                    if (availableFilters.colors.isNotEmpty()) {
                        FilterSection(
                            title = "Renk",
                            content = {
                                MultiSelectChipGroup(
                                    options = availableFilters.colors.toList(),
                                    selectedOptions = tempColors,
                                    onToggle = { color ->
                                        tempColors = if (color in tempColors) {
                                            tempColors - color
                                        } else {
                                            tempColors + color
                                        }
                                    }
                                )
                            }
                        )
                    }

                    // Etiket Section
                    if (availableFilters.tags.isNotEmpty()) {
                        FilterSection(
                            title = "Etiket",
                            content = {
                                MultiSelectChipGroup(
                                    options = availableFilters.tags.toList(),
                                    selectedOptions = tempTags,
                                    onToggle = { tag ->
                                        tempTags = if (tag in tempTags) {
                                            tempTags - tag
                                        } else {
                                            tempTags + tag
                                        }
                                    }
                                )
                            }
                        )
                    }

                    // Fiyat Aralığı Section
                    FilterSection(
                        title = "Fiyat Aralığı",
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = tempMinPrice,
                                    onValueChange = { tempMinPrice = it },
                                    label = { Text("Min") },
                                    suffix = { Text("₺") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )

                                Text("—")

                                OutlinedTextField(
                                    value = tempMaxPrice,
                                    onValueChange = { tempMaxPrice = it },
                                    label = { Text("Max") },
                                    suffix = { Text("₺") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                    )

                    // Sıralama Section
                    FilterSection(
                        title = "Sıralama",
                        content = {
                            ProductSortChips(
                                selectedSort = tempSort,
                                onSortChange = { tempSort = it }
                            )
                        }
                    )
                }

                HorizontalDivider()

                // Footer Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Temizle Button
                    OutlinedButton(
                        onClick = {
                            tempBrands = emptySet()
                            tempCategories = emptySet()
                            tempColors = emptySet()
                            tempTags = emptySet()
                            tempMinPrice = ""
                            tempMaxPrice = ""
                            tempSort = ProductSortOption.NAME_ASC
                            onClearFilters()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(Res.string.filterclearbuttontext.fromResource())
                    }

                    // Uygula Button
                    Button(
                        onClick = {
                            val minPrice = tempMinPrice.toBigDecimalOrNull()
                            val maxPrice = tempMaxPrice.toBigDecimalOrNull()
                            val priceRange = if (minPrice != null || maxPrice != null) {
                                PriceRange(minPrice, maxPrice)
                            } else null

                            onApplyFilters(
                                tempBrands,
                                tempCategories,
                                tempColors,
                                tempTags,
                                priceRange,
                                tempSort
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(Res.string.filterapplybuttontext.fromResource())
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MultiSelectChipGroup(
    options: List<String>,
    selectedOptions: Set<String>,
    onToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option in selectedOptions

            FilterChip(
                selected = isSelected,
                onClick = { onToggle(option) },
                label = { Text(option) },
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProductSortChips(
    selectedSort: ProductSortOption,
    onSortChange: (ProductSortOption) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductSortOption.entries.forEach { option ->
            FilterChip(
                selected = selectedSort == option,
                onClick = { onSortChange(option) },
                label = { Text(option.label) },
                leadingIcon = if (selectedSort == option) {
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
    }
}