package com.repzone.domain.platform

import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.util.PermissionStatus
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsConfig

/**
 * Platform Location Provider Interface
 * Her platform için farklı implementasyon gerektirir (expect/actual)
 */
expect interface IPlatformLocationProvider {
    /**
     * Tek bir GPS konumu ister
     */
    suspend fun requestLocation(timeoutSecond: Int = 15): Result<GpsLocation>

    /**
     * Sürekli konum güncellemelerini başlatır
     * @param intervalMs Milisaniye cinsinden güncelleme aralığı
     * @param minDistanceMeters Minimum mesafe değişimi (metre)
     * @param callback Her yeni konum için çağrılacak fonksiyon
     */
    fun startLocationUpdates(intervalMs: Long, minDistanceMeters: Float, callback: (GpsLocation) -> Unit)

    /**
     * Konum güncellemelerini durdurur
     */
    fun stopLocationUpdates()

    /**
     * Son bilinen konumu getirir (cache'den)
     */
    suspend fun getLastKnownLocation(): GpsLocation?

    /**
     * Konum izinlerini kontrol eder
     */
    fun checkPermissions(): PermissionStatus

    /**
     * Konum izinlerini ister
     */
    suspend fun requestPermissions(): PermissionStatus

    /**
     * Arka plan konum izni var mı kontrol eder
     */
    fun hasBackgroundPermission(): Boolean

    /**
     * Arka plan konum izni ister
     */
    suspend fun requestBackgroundPermission(): PermissionStatus

    /**
     * GPS açık mı kontrol eder
     */
    fun isLocationEnabled(): Boolean

    /**
     * Konum servisi doğruluğunu ayarlar (high/balanced/low)
     */
    fun setLocationAccuracy(priority: LocationAccuracy)
    fun setConfig(config: GpsConfig)

    suspend fun startGpsStatusMonitoring(onGpsStatusChanged: (Boolean) -> Unit)
    fun stopGpsStatusMonitoring()
}

expect interface IPlatformServiceController {
    fun startForegroundService()
    fun stopForegroundService()
    fun isServiceRunning(): Boolean
}