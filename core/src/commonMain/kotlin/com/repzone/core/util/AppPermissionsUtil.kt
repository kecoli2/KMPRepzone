package com.repzone.core.util

enum class AppPermission { Bluetooth, Notifications, Location }


sealed class PermissionStatus {
    data object Granted : PermissionStatus()
    data class Denied(val canAskAgain: Boolean) : PermissionStatus()
    data object Restricted : PermissionStatus() // iOS özel
    data object NotDetermined : PermissionStatus() // ilk kez
    object DeniedPermanently : PermissionStatus()

    val PermissionStatus.isPermanentDenied: Boolean
        get() = when (this) {
            is PermissionStatus.DeniedPermanently -> true
            is PermissionStatus.Denied -> !this.canAskAgain
            is PermissionStatus.Restricted -> true
            else -> false
        }
}