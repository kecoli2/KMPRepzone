@file:OptIn(ExperimentalMaterial3Api::class)

package com.repzone.presentation.legacy.ui.customerlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.component.FilterSection
import com.repzone.core.util.extensions.fromResource
import com.repzone.presentation.legacy.model.CustomerGroup
import com.repzone.presentation.legacy.model.enum.CustomerSortOption
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.customergroupspickertitle
import repzonemobile.core.generated.resources.filterapplybuttontext
import repzonemobile.core.generated.resources.filterclearbuttontext
import repzonemobile.core.generated.resources.short

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    selectedGroups: List<String>,
    onGroupsChange: (List<String>) -> Unit,
    selectedSort: CustomerSortOption,
    onSortChange: (CustomerSortOption) -> Unit,
    onApplyFilters: (List<String>, CustomerSortOption) -> Unit,
    onClearFilters: () -> Unit,
    customerGroups: List<CustomerGroup>
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var tempSelectedGroups by remember(selectedGroups) { mutableStateOf(selectedGroups) }
    var tempSelectedSort by remember(selectedSort) { mutableStateOf(selectedSort) }

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
                        text = "Filtreler",
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)  // Max yükseklik - button'lar için yer kalsın
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Müşteri Grupları Section
                    FilterSection(
                        title = Res.string.customergroupspickertitle.fromResource(),
                        content = {
                            CustomerGroupChips(
                                groups = customerGroups,
                                selectedGroups = tempSelectedGroups,
                                onGroupToggle = { groupId ->
                                    tempSelectedGroups = if (tempSelectedGroups.contains(groupId)) {
                                        tempSelectedGroups - groupId
                                    } else {
                                        tempSelectedGroups + groupId
                                    }
                                }
                            )
                        }
                    )

                    // Sıralama Section
                    FilterSection(
                        title = Res.string.short.fromResource(),
                        content = {
                            SortChips(
                                selectedSort = tempSelectedSort,
                                onSortChange = { tempSelectedSort = it }
                            )
                        }
                    )
                }
                HorizontalDivider()
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Temizle Button
                    OutlinedButton(
                        onClick = {
                            tempSelectedGroups = emptyList()
                            tempSelectedSort = CustomerSortOption.DATE_ASC
                            onClearFilters()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(Res.string.filterclearbuttontext.fromResource())
                    }

                    // Uygula Button
                    Button(
                        onClick = {
                            onApplyFilters(tempSelectedGroups, tempSelectedSort)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerGroupChips(
    groups: List<CustomerGroup>,
    selectedGroups: List<String>,
    onGroupToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groups.forEach { group ->
            val isSelected = selectedGroups.contains(group.id)

            FilterChip(
                selected = isSelected,
                onClick = { onGroupToggle(group.id) },
                label = { Text(group.name) },
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortChips(
    selectedSort: CustomerSortOption,
    onSortChange: (CustomerSortOption) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomerSortOption.entries.forEach { option ->
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


@Composable
fun FilterButtonWithBadge(
    filterCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = Color.White
            )
        }

        // Badge SOL ÜSTTE
        if (filterCount > 0) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 6.dp, y = 6.dp),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ) {
                Text(text = filterCount.toString())
            }
        }
    }
}