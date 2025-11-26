package com.repzone.core.util.extensions


import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.platform.NumberFormatter

// ============================================
// NumberFormatter Instance (lazy singleton)
// ============================================
private val numberFormatter by lazy { NumberFormatter() }

// ============================================
// Double Extensions
// ============================================

/**
 * Para formatına çevir: 1234.56 -> "1.234,56 ₺"
 */
fun Double?.toMoney(): String {
    return numberFormatter.formatCurrency(this ?: 0.0)
}

/**
 * Ondalık formatına çevir: 1234.567 -> "1.234,57"
 */
fun Double?.toDecimalString(decimalPlaces: Int = 2): String {
    return numberFormatter.formatDecimal(this ?: 0.0, decimalPlaces)
}

/**
 * Tam sayı formatına çevir: 1234567.89 -> "1.234.568"
 */
fun Double?.toIntegerString(): String {
    return numberFormatter.formatInteger((this ?: 0.0).toLong())
}

// ============================================
// Long/Int Extensions
// ============================================

/**
 * Tam sayı formatına çevir: 1234567 -> "1.234.567"
 */
fun Long?.toFormattedString(): String {
    return numberFormatter.formatInteger(this ?: 0L)
}

fun Int?.toFormattedString(): String {
    return numberFormatter.formatInteger((this ?: 0).toLong())
}

// ============================================
// BigDecimal Extensions
// ============================================

/**
 * BigDecimal'i para formatına çevir
 */
fun BigDecimal?.toMoney(): String {
    return numberFormatter.formatCurrency(this?.doubleValue(false) ?: 0.0)
}

/**
 * BigDecimal'i ondalık formatına çevir
 */
fun BigDecimal?.toDecimalString(decimalPlaces: Int = 2): String {
    return numberFormatter.formatDecimal(this?.doubleValue(false) ?: 0.0, decimalPlaces)
}

/**
 * BigDecimal'i tam sayı formatına çevir
 */
fun BigDecimal?.toIntegerString(): String {
    return numberFormatter.formatInteger(this?.longValue(false) ?: 0L)
}

// ============================================
// String Extensions (Parsing)
// ============================================

/**
 * Formatlanmış string'i Double'a çevir: "1.234,56" -> 1234.56
 */
fun String?.toDoubleOrNullLocalized(): Double? {
    if (this.isNullOrBlank()) return null
    return numberFormatter.parseDecimal(this)
}

/**
 * Formatlanmış string'i BigDecimal'e çevir: "1.234,56" -> BigDecimal
 */
fun String?.toBigDecimalOrNullLocalized(): BigDecimal? {
    val doubleValue = this.toDoubleOrNullLocalized() ?: return null
    return BigDecimal.fromDouble(doubleValue)
}

/**
 * Para formatındaki string'i Double'a çevir: "1.234,56 ₺" -> 1234.56
 */
fun String?.parseCurrency(): Double? {
    if (this.isNullOrBlank()) return null
    return numberFormatter.parseCurrency(this)
}

/**
 * Para formatındaki string'i BigDecimal'e çevir
 */
fun String?.parseCurrencyToBigDecimal(): BigDecimal? {
    val doubleValue = this.parseCurrency() ?: return null
    return BigDecimal.fromDouble(doubleValue)
}

// ============================================
// Backward Compatibility (eski fonksiyon adları)
// ============================================

/**
 * @deprecated Use toBigDecimalOrNullLocalized() instead
 */
@Deprecated(
    "Use toBigDecimalOrNullLocalized() for localized parsing",
    ReplaceWith("toBigDecimalOrNullLocalized()")
)
fun String?.toBigDecimalOrNull(): BigDecimal? {
    if (this.isNullOrBlank()) return null
    return try {
        // Önce localized parse dene, sonra standart
        toBigDecimalOrNullLocalized() ?: BigDecimal.parseString(this)
    } catch (e: Exception) {
        null
    }
}

// ============================================
// Utility Functions
// ============================================

/**
 * NumberFormatter'dan format config bilgilerini al
 */
object NumberFormatInfo {
    val decimalSeparator: Char get() = numberFormatter.decimalSeparator
    val groupingSeparator: Char get() = numberFormatter.groupingSeparator
    val currencySymbol: String get() = numberFormatter.currencySymbol
}
