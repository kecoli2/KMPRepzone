package com.repzone.presentation.legacy.ui.document.productlist.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.domain.document.model.ProductUnit

/**
 * Colorful badge showing unit and quantity
 * Example: "2 Koli", "5 Adet"
 */
@Composable
fun UnitBadge(
    quantity: String,
    unit: ProductUnit,
    modifier: Modifier = Modifier
) {
    if (quantity.isEmpty()) return
    
    val badgeColor = getUnitColor(unit.unitName)
    
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = badgeColor.copy(alpha = 0.15f)
    ) {
        Text(
            text = "$quantity ${unit.unitName}",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = badgeColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * Get color based on unit type
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
