package com.repzone.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.UUID

@Composable
actual fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}

/*actual class CurrencyFormatter actual constructor() {
    actual fun format(amount: Double): String {
        val value = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(amount)
        return value
    }
}*/

actual fun randomUUID(): String {
    return UUID.randomUUID().toString()
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NumberFormatter {
    private val locale: Locale = Locale.getDefault()
    private val symbols = DecimalFormatSymbols(locale)

    actual val decimalSeparator: Char = symbols.decimalSeparator
    actual val groupingSeparator: Char = symbols.groupingSeparator
    actual val currencySymbol: String = "₺"

    private val currencyFormat = (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).apply {
        currency = Currency.getInstance(locale)
    }

    private val decimalFormat = DecimalFormat("#,##0.##", symbols)
    private val integerFormat = DecimalFormat("#,##0", symbols)

    actual fun formatCurrency(amount: Double): String {
        return currencyFormat.format(amount)
    }

    actual fun formatDecimal(amount: Double, decimalPlaces: Int): String {
        val pattern = if (decimalPlaces > 0) {
            "#,##0.${"0".repeat(decimalPlaces)}"
        } else {
            "#,##0"
        }
        return DecimalFormat(pattern, symbols).format(amount)
    }

    actual fun formatInteger(amount: Long): String {
        return integerFormat.format(amount)
    }

    actual fun parseCurrency(text: String): Double? {
        return try {
            // Binlik ayırıcıları kaldır, ondalık ayırıcıyı '.' yap
            val normalized = text
                .replace(currencySymbol, "")
                .replace(groupingSeparator.toString(), "")
                .replace(decimalSeparator, '.')
                .trim()
            normalized.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }

    actual fun parseDecimal(text: String): Double? {
        return try {
            val normalized = text
                .replace(groupingSeparator.toString(), "")
                .replace(decimalSeparator, '.')
                .trim()
            normalized.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
