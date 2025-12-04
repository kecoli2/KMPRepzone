package com.repzone.presentation.legacy.ui.document.basket.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.model.StringResource
import com.repzone.core.ui.component.card.CardHeaderStyle
import com.repzone.core.ui.component.card.CardVariant
import com.repzone.core.ui.component.card.ExpandableCard
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toCleanString
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.document.model.BasketStatistics
import com.repzone.domain.document.model.UnitStatistic

/**
 * Basket statistics card with expandable sections
 */
@Composable
fun BasketStatisticsCard(
    statistics: BasketStatistics,
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false
) {
    if (statistics.isEmpty) return

    var expanded by remember { mutableStateOf(initialExpanded) }

    ExpandableCard(
        title = StringResource.BASKET_SUMMARY.fromResource(),
        subtitle = buildSubtitle(statistics),
        leadingIcon = Icons.Default.Analytics,
        expanded = expanded,
        modifier = modifier.fillMaxWidth(),
        variant = CardVariant.OUTLINED,
        headerStyle = CardHeaderStyle.COMPACT,
        onExpandChange = { expanded = it },
        borderWidth = 0.5.dp,
        elevation = 4.dp

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Miktar Dağılımı
            StatisticsSection(
                title = StringResource.BASKET_LINES_DISTRIBUTION.fromResource(),
                icon = Icons.Outlined.Inventory2
            ) {
                UnitBreakdownRow(
                    unitBreakdown = statistics.unitBreakdown,
                    totalBaseQuantity = statistics.totalBaseQuantity
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Finansal Özet
            StatisticsSection(
                title = "Finansal Özet",
                icon = Icons.Outlined.Payments
            ) {
                FinancialSummary(statistics = statistics)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Çeşitlilik & Lojistik
            StatisticsSection(
                title = "Detaylar",
                icon = Icons.Outlined.Category
            ) {
                VarietyAndLogistics(statistics = statistics)
            }
        }
    }
}

@Composable
private fun StatisticsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        content()
    }
}

@Composable
private fun UnitBreakdownRow(
    unitBreakdown: List<UnitStatistic>,
    totalBaseQuantity: BigDecimal
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Unit badges in a flow row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            unitBreakdown.forEach { unit ->
                UnitChip(
                    unitName = unit.unitName,
                    quantity = unit.quantity
                )
            }
        }

        // Base quantity summary
        Text(
            text = "Toplam: ${totalBaseQuantity.toCleanString()} adet (base birim)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UnitChip(
    unitName: String,
    quantity: BigDecimal
) {
    val chipColor = getUnitColor(unitName)

    Surface(
        shape = MaterialTheme.shapes.small,
        color = chipColor.copy(alpha = 0.1f)
    ) {
        Text(
            text = "${quantity.toCleanString()} $unitName",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = chipColor
        )
    }
}

@Composable
private fun FinancialSummary(statistics: BasketStatistics) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Brüt Tutar
        FinancialRow(
            label = "Brüt Tutar",
            value = statistics.grossTotal.doubleValue(false).toMoney()
        )

        // İndirim (varsa)
        if (statistics.hasDiscount) {
            FinancialRow(
                label = "İndirim",
                value = "-${statistics.discountAmount.doubleValue(false).toMoney()}",
                subLabel = "(${statistics.discountPercentage.toCleanString()}%)",
                valueColor = Color(0xFF10B981) // Yeşil
            )
        }

        // Net Tutar
        FinancialRow(
            label = "Net Tutar",
            value = statistics.netTotal.doubleValue(false).toMoney()
        )

        // KDV
        FinancialRow(
            label = "KDV",
            value = statistics.vatTotal.doubleValue(false).toMoney()
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Genel Toplam
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TOPLAM",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = statistics.grandTotal.doubleValue(false).toMoney(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FinancialRow(
    label: String,
    value: String,
    subLabel: String? = null,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
private fun VarietyAndLogistics(statistics: BasketStatistics) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Çeşitlilik satırı
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VarietyItem(
                icon = Icons.Outlined.Inventory2,
                value = statistics.productCount.toString(),
                label = "Ürün"
            )
            VarietyItem(
                icon = Icons.Outlined.Store,
                value = statistics.brandCount.toString(),
                label = "Marka"
            )
            VarietyItem(
                icon = Icons.Outlined.Category,
                value = statistics.groupCount.toString(),
                label = "Grup"
            )
        }

        // Tonaj bilgisi
        if (statistics.totalWeight.compare(BigDecimal.ZERO) > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalShipping,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tonaj: ${statistics.totalWeight.toCleanString()} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun VarietyItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Build subtitle for collapsed state
 */
private fun buildSubtitle(statistics: BasketStatistics): String {
    return "${statistics.lineCount} satır • ${statistics.productCount} ürün • ${statistics.brandCount} marka"
}

/**
 * Get color based on unit type (matching UnitBadge colors)
 */
private fun getUnitColor(unitName: String): Color {
    return when (unitName.lowercase()) {
        "adet", "ad", "piece", "pcs" -> Color(0xFF4CAF50)      // Green
        "koli", "box", "carton" -> Color(0xFF2196F3)           // Blue
        "palet", "palette", "pallet" -> Color(0xFFFF9800)      // Orange
        "kg", "kilogram" -> Color(0xFF9C27B0)                  // Purple
        "lt", "litre", "liter" -> Color(0xFF00BCD4)           // Cyan
        "mt", "metre", "meter" -> Color(0xFFFF5722)           // Deep Orange
        "paket", "package", "pack" -> Color(0xFF607D8B)        // Blue Grey
        else -> Color(0xFF9E9E9E)                              // Grey
    }
}