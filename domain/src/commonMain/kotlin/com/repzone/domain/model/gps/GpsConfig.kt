package com.repzone.domain.model.gps
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.common.Result

/**
 * Gps Configuration sonrasında calisma anında degisebilir default
 */
data class GpsConfig(
    val gpsIntervalMinutes: Int = 5, // GPS toplama aralığı
    val serverSyncIntervalMinutes: Int = 15, // Sync aralığı
    val minDistanceMeters: Float = 10f,  // Min hareket mesafesi
    val accuracyThreshold: Float = 50f, // Max GPS hatası
    val batteryOptimizationEnabled: Boolean = true,
    val enableBackgroundTracking: Boolean = true
){
    fun validate(): Result<Unit> {
        return when {
            gpsIntervalMinutes < 1 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_INTERVAL_TOO_SHORT))
            serverSyncIntervalMinutes < 1 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.SYNC_INTERVAL_TOO_SHORT))
            minDistanceMeters < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_MIN_DISTANCE_NAGATIVE))
            accuracyThreshold < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_ACCURACY_THRESHOLD_NEGATIVE))
            else -> Result.Success(Unit)
        }
    }
}