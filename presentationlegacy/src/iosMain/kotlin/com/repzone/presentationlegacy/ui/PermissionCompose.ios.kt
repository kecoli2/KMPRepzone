package com.repzone.presentationlegacy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.repzone.presentationlegacy.permissions.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    // iOS tarafındaki PermissionManager parametresizdi:
    return remember { PermissionManager() }
}