package com.repzone.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import java.text.NumberFormat
import java.util.Locale

@Composable
actual fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}

actual class CurrencyFormatter actual constructor() {
    actual fun format(amount: Double): String {
        val value = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .format(amount)
        return value
    }
}