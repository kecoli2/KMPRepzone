package com.repzone.core.platform

import androidx.compose.runtime.Composable

@Composable
actual fun isInPreview(): Boolean {
    return false
}