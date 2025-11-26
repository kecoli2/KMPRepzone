package com.repzone.core.platform

import androidx.compose.runtime.Composable
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSUUID
import platform.Foundation.currentLocale

@Composable
actual fun isInPreview(): Boolean {
    return false
}

/*
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CurrencyFormatter actual constructor() {
    actual fun format(amount: Double): String {
        val formatter = NSNumberFormatter()
        formatter.numberStyle = NSNumberFormatterCurrencyStyle
        return formatter.stringFromNumber(NSNumber(amount)) ?: ""
    }
}
*/

actual fun randomUUID(): String {
    return NSUUID().UUIDString()
}

actual class NumberFormatter {
    private val locale = NSLocale.currentLocale
    private val formatter = NSNumberFormatter().apply {
        this.locale = this@NumberFormatter.locale
        numberStyle = NSNumberFormatterDecimalStyle
    }

    private val currencyFormatter = NSNumberFormatter().apply {
        this.locale = this@NumberFormatter.locale
        numberStyle = NSNumberFormatterCurrencyStyle
        currencyCode = "TRY"
        currencySymbol = "₺"
    }

    actual val decimalSeparator: Char = formatter.decimalSeparator.first()
    actual val groupingSeparator: Char = formatter.groupingSeparator.first()
    actual val currencySymbol: String = "₺"

    actual fun formatCurrency(amount: Double): String {
        return currencyFormatter.stringFromNumber(NSNumber(double = amount)) ?: "0"
    }

    actual fun formatDecimal(amount: Double, decimalPlaces: Int): String {
        formatter.minimumFractionDigits = decimalPlaces.toULong()
        formatter.maximumFractionDigits = decimalPlaces.toULong()
        return formatter.stringFromNumber(NSNumber(double = amount)) ?: "0"
    }

    actual fun formatInteger(amount: Long): String {
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 0u
        return formatter.stringFromNumber(NSNumber(longLong = amount)) ?: "0"
    }

    actual fun parseCurrency(text: String): Double? {
        val normalized = text
            .replace(currencySymbol, "")
            .replace(groupingSeparator.toString(), "")
            .replace(decimalSeparator, '.')
            .trim()
        return normalized.toDoubleOrNull()
    }

    actual fun parseDecimal(text: String): Double? {
        val normalized = text
            .replace(groupingSeparator.toString(), "")
            .replace(decimalSeparator, '.')
            .trim()
        return normalized.toDoubleOrNull()
    }
}