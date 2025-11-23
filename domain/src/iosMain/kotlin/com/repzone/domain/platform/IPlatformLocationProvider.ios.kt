package com.repzone.domain.platform

import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.util.PermissionStatus
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsConfig

actual interface IPlatformLocationProvider {
    actual suspend fun requestLocation(timeoutSecond: Int): Result<GpsLocation>
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
    actual fun setConfig(config: GpsConfig)
    actual suspend fun requestEnableLocation(): Boolean
    actual suspend fun startGpsStatusMonitoring(onGpsStatusChanged: (Boolean) -> Unit)
    actual fun stopGpsStatusMonitoring()
}

actual interface IPlatformServiceController {
    actual fun startForegroundService()
    actual fun stopForegroundService()
    actual fun isServiceRunning(): Boolean
}