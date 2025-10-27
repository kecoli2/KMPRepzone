package com.repzone.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun isInPreview(): Boolean

expect class CurrencyFormatter() {
    fun format(amount: Double): String
}