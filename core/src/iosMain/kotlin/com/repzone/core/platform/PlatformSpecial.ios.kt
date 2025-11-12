package com.repzone.core.platform

import androidx.compose.runtime.Composable
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSUUID

@Composable
actual fun isInPreview(): Boolean {
    return false
}

actual class CurrencyFormatter actual constructor() {
    actual fun format(amount: Double): String {
        val formatter = NSNumberFormatter()
        formatter.numberStyle = NSNumberFormatterCurrencyStyle
        return formatter.stringFromNumber(NSNumber(amount)) ?: ""
    }
}

actual fun randomUUID(): String {
    return NSUUID().UUIDString()
}