package com.repzone.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun isInPreview(): Boolean
expect fun randomUUID(): String

/**
 * Platform-specific number/currency formatter
 * Android: java.text.DecimalFormat kullanır
 * iOS: NSNumberFormatter kullanır
 */
expect class NumberFormatter() {
    /** Ondalık ayırıcı (TR: ',' EN: '.') */
    val decimalSeparator: Char

    /** Binlik ayırıcı (TR: '.' EN: ',') */
    val groupingSeparator: Char

    /** Para birimi sembolü (TR: '₺' EN: '$') */
    val currencySymbol: String

    /** Para formatla: 1234.56 -> "1.234,56 ₺" */
    fun formatCurrency(amount: Double): String

    /** Ondalık formatla: 1234.567 -> "1.234,57" */
    fun formatDecimal(amount: Double, decimalPlaces: Int = 2): String

    /** Tam sayı formatla: 1234567 -> "1.234.567" */
    fun formatInteger(amount: Long): String

    /** Para parse et: "1.234,56" -> 1234.56 */
    fun parseCurrency(text: String): Double?

    /** Ondalık parse et: "1.234,56" -> 1234.56 */
    fun parseDecimal(text: String): Double?
}

// Backward compatibility için typealias
@Deprecated("Use NumberFormatter instead", ReplaceWith("NumberFormatter"))
typealias CurrencyFormatter = NumberFormatter
