package com.repzone.domain.service

import com.repzone.core.enums.GpsStatus
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import kotlinx.coroutines.flow.Flow
import com.repzone.domain.common.Result

/**
 * Location service
 */
interface ILocationService {
    suspend fun startService(config: GpsConfig): Result<Unit>
    suspend fun stopService(): Result<Unit>
    suspend fun pauseService(): Result<Unit>
    suspend fun resumeService(): Result<Unit>
    fun isServiceRunning(): Boolean
    suspend fun getCurrentLocation(): Result<GpsLocation>
    suspend fun forceGpsUpdate(): Result<GpsLocation>
    suspend fun getLastKnownLocation(): GpsLocation?
    fun observeLocationUpdates(): Flow<GpsLocation>
    fun observeServiceState(): Flow<ServiceState>
    fun observeGpsStatus(): Flow<GpsStatus>
    suspend fun requestEnableGps(): Boolean
}