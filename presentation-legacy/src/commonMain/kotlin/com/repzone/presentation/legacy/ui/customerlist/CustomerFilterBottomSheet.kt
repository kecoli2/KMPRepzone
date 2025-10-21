@file:OptIn(ExperimentalMaterial3Api::class)

package com.repzone.presentation.legacy.ui.customerlist

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
import com.repzone.core.util.extensions.fromResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.customergroupspickertitle
import repzonemobile.core.generated.resources.filterapplybuttontext
import repzonemobile.core.generated.resources.filterclearbuttontext
import repzonemobile.core.generated.resources.short

data class CustomerGroup(
    val id: String,
    val name: String
)

enum class SortOption(val label: String) {
    NAME_ASC("İsim (A-Z)"),
    NAME_DESC("İsim (Z-A)"),
    DATE_ASC("Tarih (Eskiden Yeniye)"),
    DATE_DESC("Tarih (Yeniden Eskiye)")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    selectedGroups: List<String>,
    onGroupsChange: (List<String>) -> Unit,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    customerGroups: List<CustomerGroup> = getDefaultCustomerGroups()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                                selectedGroups = selectedGroups,
                                onGroupToggle = { groupId ->
                                    val newSelection = if (selectedGroups.contains(groupId)) {
                                        selectedGroups - groupId
                                    } else {
                                        selectedGroups + groupId
                                    }
                                    onGroupsChange(newSelection)
                                }
                            )
                        }
                    )

                    // Sıralama Section
                    FilterSection(
                        title = Res.string.short.fromResource(),
                        content = {
                            SortChips(
                                selectedSort = selectedSort,
                                onSortChange = onSortChange
                            )
                        }
                    )
                }
                HorizontalDivider()
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Temizle Button
                    OutlinedButton(
                        onClick = {
                            onGroupsChange(emptyList())
                            onSortChange(SortOption.NAME_ASC)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(Res.string.filterclearbuttontext.fromResource())
                    }

                    // Uygula Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(Res.string.filterapplybuttontext.fromResource())
                    }
                }
            }
        }
    }
}
@Composable
fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        content()
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
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortOption.entries.forEach { option ->
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

fun getDefaultCustomerGroups(): List<CustomerGroup> {
    return listOf(
        CustomerGroup("1", "A Grubu"),
        CustomerGroup("2", "B Grubu"),
        CustomerGroup("3", "C Grubu"),
        CustomerGroup("4", "VIP Müşteriler"),
        CustomerGroup("5", "Potansiyel Müşteriler"),
        CustomerGroup("6", "Aktif Müşteriler"),
        CustomerGroup("7", "Pasif Müşteriler")
    )
}