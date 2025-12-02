package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class ProductStatistics(
    val totalEntryCount: Int = 0,
    val totalAmountWithoutDiscount: BigDecimal = BigDecimal.ZERO,
    val groups: List<ProductGroupStatistic> = emptyList()
) {
    val groupCount: Int get() = groups.size

    val isEmpty: Boolean get() = totalEntryCount == 0

    companion object {
        val EMPTY = ProductStatistics()
    }
}

data class ProductGroupStatistic(
    val groupId: Int,
    val groupName: String,
    val groupColor: Long, // Color as ARGB long value
    val entryCount: Int,
    val totalAmount: BigDecimal,
    val unitBreakdown: List<UnitStatistic>
) {
    companion object {
        // Predefined colors for groups (will cycle through these)
        val GROUP_COLORS = listOf(
            0xFF6366F1, // Indigo
            0xFFF59E0B, // Amber
            0xFF10B981, // Emerald
            0xFFEF4444, // Red
            0xFF8B5CF6, // Violet
            0xFF06B6D4, // Cyan
            0xFFF97316, // Orange
            0xFF84CC16, // Lime
            0xFFEC4899, // Pink
            0xFF14B8A6, // Teal
        )

        fun getColorForIndex(index: Int): Long {
            return GROUP_COLORS[index % GROUP_COLORS.size]
        }
    }
}

data class UnitStatistic(
    val unitName: String,
    val quantity: BigDecimal,
    val amount: BigDecimal
) {
    companion object {
        fun getUnitColor(unitName: String): Long {
            return when (unitName.lowercase()) {
                "adet", "ad", "piece", "pcs" -> 0xFF4CAF50      // Green
                "koli", "box", "carton" -> 0xFF2196F3           // Blue
                "palet", "palette", "pallet" -> 0xFFFF9800      // Orange
                "kg", "kilogram" -> 0xFF9C27B0                  // Purple
                "lt", "litre", "liter" -> 0xFF00BCD4            // Cyan
                "mt", "metre", "meter" -> 0xFFFF5722            // Deep Orange
                "paket", "package", "pack" -> 0xFF607D8B        // Blue Grey
                else -> 0xFF9E9E9E                              // Grey
            }
        }
    }
}