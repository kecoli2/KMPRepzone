package com.repzone.presentation.legacy.ui.document.basket.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.model.StringResource
import com.repzone.core.platform.NumberFormatter
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toCleanString
import com.repzone.domain.document.model.BasketStatistics
import com.repzone.domain.document.model.UnitStatistic

private val FAB_SIZE = 56.dp
private val FAB_MARGIN = 16.dp
private val FAB_TOTAL_SPACE = FAB_SIZE + FAB_MARGIN + 8.dp

/**
 * Bottom sticky panel showing basket totals
 * Collapsed: Shows grand total, net total, discount info
 * Expanded: Shows full financial breakdown + variety info
 */
@Composable
fun BasketTotalsPanel(
    statistics: BasketStatistics,
    modifier: Modifier = Modifier,
    fabSpacing: Boolean = true,
    onExpended: (Boolean) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val formatter = remember { NumberFormatter() }

    if (statistics.isEmpty) return




    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(
                    topStart = if (isExpanded) 20.dp else 0.dp,
                    topEnd = if (isExpanded) 20.dp else 0.dp
                )
            ),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(
            topStart = if (isExpanded) 20.dp else 0.dp,
            topEnd = if (isExpanded) 20.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(300))
        ) {
            // Top accent border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        if (isExpanded) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
            )

            if (isExpanded) {
                ExpandedContent(
                    statistics = statistics,
                    formatter = formatter,
                    onCollapseClick = {
                        isExpanded = false
                        onExpended(isExpanded) },
                    fabSpacing = fabSpacing
                )
            } else {
                CollapsedContent(
                    statistics = statistics,
                    formatter = formatter,
                    onExpandClick = {
                        isExpanded = true
                        onExpended(isExpanded)}
                )
            }
        }
    }
}

@Composable
private fun CollapsedContent(
    statistics: BasketStatistics,
    formatter: NumberFormatter,
    onExpandClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Brüt Tutar
        FinancialRow(
            label = StringResource.GROSS.fromResource(),
            value = formatter.formatCurrency(statistics.grossTotal.doubleValue(false)),
            valueColor = MaterialTheme.colorScheme.onSurface
        )

        // İndirim (varsa)
        FinancialRow(
            label = StringResource.DISCOUNT_PERCENT_SUMMARY.fromResource(statistics.discountPercentage.toCleanString()) ,
            value = "-${formatter.formatCurrency(statistics.discountAmount.doubleValue(false))}",
            valueColor = Color(0xFF10B981)
        )

        // Net Tutar
        FinancialRow(
            label = StringResource.NETSALE.fromResource(),
            value = formatter.formatCurrency(statistics.netTotal.doubleValue(false)),
            valueColor = MaterialTheme.colorScheme.onSurface
        )

        // KDV
        FinancialRow(
            label = StringResource.VAT_TOTAL.fromResource(),
            value = "+${formatter.formatCurrency(statistics.vatTotal.doubleValue(false))}",
            valueColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Grand Total + Expand button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onExpandClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = StringResource.TOTAL_AMOUNT.fromResource(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatter.formatCurrency(statistics.grandTotal.doubleValue(false)),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Expand indicator
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = StringResource.DETAIL.fromResource(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Genişlet",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandedContent(
    statistics: BasketStatistics,
    formatter: NumberFormatter,
    onCollapseClick: () -> Unit,
    fabSpacing: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCollapseClick)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = StringResource.BASKET_SUMMARY.fromResource(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Line count badge
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = StringResource.LINE_SUMMARY_INFO.fromResource(statistics.lineCount),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringResource.CLOSE.fromResource(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Daralt",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Extra Details (only visible when expanded)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Miktar Dağılımı
            if (statistics.unitBreakdown.isNotEmpty()) {
                SectionTitle(title = StringResource.BASKET_LINES_DISTRIBUTION.fromResource(), icon = Icons.Outlined.Inventory2)
                UnitBreakdownSection(
                    unitBreakdown = statistics.unitBreakdown,
                    totalBaseQuantity = statistics.totalBaseQuantity
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Çeşitlilik & Lojistik
            VarietySection(statistics = statistics)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // Financial Summary (same as collapsed, but with collapse button area)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Brüt Tutar
            FinancialRow(
                label = StringResource.GROSS.fromResource(),
                value = formatter.formatCurrency(statistics.grossTotal.doubleValue(false)),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            // İndirim (varsa)
            FinancialRow(
                label = StringResource.DISCOUNT_PERCENT_SUMMARY.fromResource(statistics.discountPercentage.toCleanString()) ,
                value = "-${formatter.formatCurrency(statistics.discountAmount.doubleValue(false))}",
                valueColor = Color(0xFF10B981)
            )

            // Net Tutar
            FinancialRow(
                label = StringResource.NETSALE.fromResource(),
                value = formatter.formatCurrency(statistics.netTotal.doubleValue(false)),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            // KDV
            FinancialRow(
                label = StringResource.VAT_TOTAL.fromResource(),
                value = "+${formatter.formatCurrency(statistics.vatTotal.doubleValue(false))}",
                valueColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Grand Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringResource.TOTAL_AMOUNT.fromResource(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatter.formatCurrency(statistics.grandTotal.doubleValue(false)),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // FAB spacing
            if (fabSpacing) {
                Spacer(modifier = Modifier.height(FAB_TOTAL_SPACE))
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    icon: ImageVector?
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun UnitBreakdownSection(
    unitBreakdown: List<UnitStatistic>,
    totalBaseQuantity: BigDecimal
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Unit chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            unitBreakdown.take(4).forEach { unit ->
                val unitColor = getUnitColor(unit.unitName)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = unitColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "${unit.quantity.toCleanString()} ${unit.unitName}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = unitColor
                    )
                }
            }
        }

        // Base quantity
        Text(
            text = StringResource.TOTAL_BASE_QUANTITY.fromResource(totalBaseQuantity.toCleanString()),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FinancialRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
private fun VarietySection(statistics: BasketStatistics) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Variety row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VarietyItem(
                icon = Icons.Outlined.Inventory2,
                value = statistics.productCount.toString(),
                label = StringResource.PRODUCT.fromResource()
            )
            VarietyItem(
                icon = Icons.Outlined.Store,
                value = statistics.brandCount.toString(),
                label = StringResource.BRANDS.fromResource()
            )
            VarietyItem(
                icon = Icons.Outlined.Category,
                value = statistics.groupCount.toString(),
                label = StringResource.GROUP.fromResource()
            )
        }

        // Weight info
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
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = StringResource.CARTSUMMARYTONAGE.fromResource() + " ${statistics.totalWeight.toCleanString()} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
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
                modifier = Modifier.size(16.dp),
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
 * Get color based on unit type
 */
private fun getUnitColor(unitName: String): Color {
    return when (unitName.lowercase()) {
        "adet", "ad", "piece", "pcs" -> Color(0xFF4CAF50)
        "koli", "box", "carton" -> Color(0xFF2196F3)
        "palet", "palette", "pallet" -> Color(0xFFFF9800)
        "kg", "kilogram" -> Color(0xFF9C27B0)
        "lt", "litre", "liter" -> Color(0xFF00BCD4)
        "mt", "metre", "meter" -> Color(0xFFFF5722)
        "paket", "package", "pack" -> Color(0xFF607D8B)
        else -> Color(0xFF9E9E9E)
    }
}