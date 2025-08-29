package com.repzone.mobile.compose.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.repzone.mobile.managers.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    // iOS tarafındaki PermissionManager parametresizdi:
    return remember { PermissionManager() }
}