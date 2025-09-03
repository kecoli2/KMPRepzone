package com.repzone.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.repzone.presentation.manager.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    // iOS tarafındaki PermissionManager parametresizdi:
    return remember { PermissionManager() }
}