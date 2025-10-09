package com.repzone.presentation.legacy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.repzone.presentation.legacy.manager.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    // iOS tarafındaki PermissionManager parametresizdi:
    return remember { PermissionManager() }
}