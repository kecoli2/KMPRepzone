package com.repzone.mobile.managers

import com.repzone.core.util.PermissionStatus

expect class PermissionManager {
    suspend fun ensureBluetooth(): PermissionStatus
    suspend fun ensureNotifications(): PermissionStatus
    suspend fun ensureLocation(): PermissionStatus

    suspend fun checkBluetooth(): PermissionStatus
    suspend fun checkNotifications(): PermissionStatus
    suspend fun checkLocation(): PermissionStatus
}