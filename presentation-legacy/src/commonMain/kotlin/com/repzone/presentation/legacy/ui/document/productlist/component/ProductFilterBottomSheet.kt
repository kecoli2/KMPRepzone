@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.repzone.presentation.legacy.ui.document.productlist.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.core.model.StringResource
import com.repzone.core.ui.component.FilterSection
import com.repzone.core.ui.component.textfield.CurrencyTextField
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toBigDecimalOrNullLocalized
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.model.product.ProductFilters
import com.repzone.presentation.legacy.model.enum.ProductSortOption
import repzonemobile.core.generated.resources.*

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
                        text = StringResource.FILTER_PRODUCTS.fromResource(),
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
                            title = StringResource.FILTERORDERTYPEBRAND.fromResource(),
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
                    if (availableFilters.group.isNotEmpty()) {
                        FilterSection(
                            title = StringResource.FILTER_CATEGORY.fromResource(),
                            content = {
                                MultiSelectChipGroup(
                                    options = availableFilters.group.toList(),
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
                            title = StringResource.FILTER_COLOR.fromResource(),
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
                            title = StringResource.TAGS.fromResource(),
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
                        title = StringResource.FILTERPRICERANGETEXT.fromResource(),
                        content = {
                            PriceRangeInput(
                                minPrice = tempMinPrice,
                                maxPrice = tempMaxPrice,
                                onMinPriceChange = { tempMinPrice = it },
                                onMaxPriceChange = { tempMaxPrice = it }
                            )
                        }
                    )

                    // Sıralama Section
                    FilterSection(
                        title = StringResource.SHORT.fromResource(),
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
                            val minPrice = tempMinPrice.toBigDecimalOrNullLocalized()
                            val maxPrice = tempMaxPrice.toBigDecimalOrNullLocalized()
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

/**
 * Fiyat aralığı input komponenti
 */
@Composable
private fun PriceRangeInput(
    minPrice: String,
    maxPrice: String,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Min Price
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Min",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            CurrencyTextField(
                value = minPrice,
                onValueChange = onMinPriceChange,
                placeholder = "0",
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                cornerRadius = 12.dp,
                borderWidth = 1.dp
            )
        }

        // Separator
        Text(
            text = "—",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 24.dp)
        )

        // Max Price
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Max",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            CurrencyTextField(
                value = maxPrice,
                onValueChange = onMaxPriceChange,
                placeholder = "0",
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                cornerRadius = 12.dp,
                borderWidth = 1.dp
            )
        }
    }
}

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