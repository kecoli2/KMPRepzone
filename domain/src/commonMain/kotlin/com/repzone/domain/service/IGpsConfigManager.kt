package com.repzone.domain.service

import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsConfig
import kotlinx.coroutines.flow.Flow

interface IGpsConfigManager {
    fun getConfig(): GpsConfig
    suspend fun updateConfig(config: GpsConfig): Result<Unit>
    fun observeConfig(): Flow<GpsConfig>
    suspend fun updateGpsInterval(minutes: Int): Result<Unit>
    suspend fun updateSyncInterval(minutes: Int): Result<Unit>
    fun updateMinDistance(meters: Float): Result<Unit>
    fun updateAccuracyThreshold(meters: Float): Result<Unit>
    fun enableBatteryOptimization(enabled: Boolean)
    fun enableBackgroundTracking(enabled: Boolean)
    fun resetToDefaults()
}