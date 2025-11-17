package com.repzone.core.ui.manager.permissions

import com.repzone.core.util.PermissionStatus

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PermissionManager {
    suspend fun ensureBluetooth(): PermissionStatus
    suspend fun ensureNotifications(): PermissionStatus
    suspend fun ensureLocation(): PermissionStatus

    suspend fun ensureForegroundService(): PermissionStatus
    suspend fun checkForegroundService(): PermissionStatus


    suspend fun checkBluetooth(): PermissionStatus
    suspend fun checkNotifications(): PermissionStatus
    suspend fun checkLocation(): PermissionStatus
    fun openAppSettings()

}

