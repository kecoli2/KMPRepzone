package com.repzone.domain.platform

import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.util.PermissionStatus
import com.repzone.domain.model.gps.GpsLocation

actual interface IPlatformLocationProvider {
    actual suspend fun requestLocation(): Result<GpsLocation>
    actual fun startLocationUpdates(
        intervalMs: Long,
        minDistanceMeters: Float,
        callback: (GpsLocation) -> Unit
    )

    actual fun stopLocationUpdates()
    actual suspend fun getLastKnownLocation(): GpsLocation?
    actual fun checkPermissions(): PermissionStatus
    actual suspend fun requestPermissions(): PermissionStatus
    actual fun hasBackgroundPermission(): Boolean
    actual suspend fun requestBackgroundPermission(): PermissionStatus
    actual fun isLocationEnabled(): Boolean
    actual fun setLocationAccuracy(priority: LocationAccuracy)

}