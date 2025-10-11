package com.repzone.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
actual fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}