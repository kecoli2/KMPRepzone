package com.repzone.domain.service

import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import kotlinx.coroutines.flow.Flow

/**
 * Location service
 */
interface ILocationService {
    /**
     * Servis Kontrolleri
     */
    suspend fun startService(config: GpsConfig)
    suspend fun stopService()
    fun isServiceRunning(): Boolean

    /**
     * Gps Operasyonlari
     */
    suspend fun getCurrentLocation(): Result<GpsLocation>
    suspend fun forceGpsUpdate(): Result<GpsLocation>
    suspend fun getLastKnownLocation(): GpsLocation?

    /**
     * Gps Monitoring islemleri
     */
    fun observeLocationUpdates(): Flow<GpsLocation>
    fun observeServiceState(): Flow<ServiceState>
}