package com.repzone.data.repository.imp

import com.repzone.core.interfaces.IUserSession
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.service.IGpsConfigManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *  Sorumluluk: Konfigürasyon yönetimi
 *  Özellikler:
 *
 * MutableStateFlow ile reactive config
 * Validation kontrolü
 * TODO: SharedPreferences/UserDefaults persistence
 */
class GpsConfigManagerImpl(private val iUserSession: IUserSession): IGpsConfigManager {
    //region Field
    private val _config = MutableStateFlow(GpsConfig())
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getConfig(): GpsConfig {
        return _config.value
    }
    override suspend fun updateConfig(config: GpsConfig): Result<Unit> {
        // Validasyon kontrolü
        val validationResult = config.validate()
        if (validationResult.isSuccess) {
            return validationResult
        }

        _config.value = config
        // TODO: Konfigürasyonu persist et (SharedPreferences/UserDefaults)
        return Result.Success(Unit)
    }

    override suspend fun updateGpsInterval(minutes: Int): Result<Unit> {
        if (minutes < 1) {
            return Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_INTERVAL_TOO_SHORT))
        }

        val currentConfig = _config.value
        return updateConfig(currentConfig.copy(gpsIntervalMinutes = minutes))
    }

    override suspend fun updateSyncInterval(minutes: Int): Result<Unit> {
        if (minutes < 1) {
            return Result.Error(DomainException.BusinessRuleException(ErrorCode.SYNC_INTERVAL_TOO_SHORT))
        }

        val currentConfig = _config.value
        return updateConfig(currentConfig.copy(serverSyncIntervalMinutes = minutes))
    }

    override fun observeConfig(): Flow<GpsConfig> {
        return _config.asStateFlow()
    }

    override fun updateMinDistance(meters: Float): Result<Unit> {
        if (meters < 0) {
            return Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_MIN_DISTANCE_NAGATIVE))
        }
        val currentConfig = _config.value
        _config.value = currentConfig.copy(minDistanceMeters = meters)
        return Result.Success(Unit)
    }

    override fun updateAccuracyThreshold(meters: Float): Result<Unit> {
        if (meters < 0) {
            return Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_ACCURACY_THRESHOLD_NEGATIVE))
        }
        val currentConfig = _config.value
        _config.value = currentConfig.copy(accuracyThreshold = meters)
        return Result.Success(Unit)
    }

    override fun enableBatteryOptimization(enabled: Boolean) {
        val currentConfig = _config.value
        _config.value = currentConfig.copy(batteryOptimizationEnabled = enabled)
    }

    override fun enableBackgroundTracking(enabled: Boolean) {
        val currentConfig = _config.value
        _config.value = currentConfig.copy(enableBackgroundTracking = enabled)
    }

    override fun resetToDefaults() {
        _config.value = GpsConfig()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}