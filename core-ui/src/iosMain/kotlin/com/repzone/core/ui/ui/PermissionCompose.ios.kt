package com.repzone.core.ui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.repzone.core.ui.manager.permissions.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    // iOS tarafındaki PermissionManager parametresizdi:
    return remember { PermissionManager() }
}