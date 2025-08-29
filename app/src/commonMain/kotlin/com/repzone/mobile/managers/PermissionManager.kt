package com.repzone.mobile.managers

import com.repzone.core.util.PermissionStatus

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PermissionManager {
    suspend fun ensureBluetooth(): PermissionStatus
    suspend fun ensureNotifications(): PermissionStatus
    suspend fun ensureLocation(): PermissionStatus

    suspend fun checkBluetooth(): PermissionStatus
    suspend fun checkNotifications(): PermissionStatus
    suspend fun checkLocation(): PermissionStatus
}