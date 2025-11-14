package com.repzone.domain.manager.gps

import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.LocationMetadata
import com.repzone.domain.model.gps.ServiceState
import kotlinx.coroutines.flow.Flow

/**
 * Tüm servisleri koordine eden ana interface
 */
interface IGpsTrackingManager {
    // Lifecycle
    suspend fun initialize(config: GpsConfig)
    suspend fun start()
    suspend fun stop()
    suspend fun pause()
    suspend fun resume()

    /**
     * Quick actions
     */
    suspend fun forceGpsUpdate(): Result<GpsLocation>

    /**
     * Metadata queries
     */
    suspend fun getLocationMetadata(): LocationMetadata
    suspend fun getLastLocation(): GpsLocation?

    /**
     * Observables
     */
    fun observeServiceState(): Flow<ServiceState>
    fun observeLastLocation(): Flow<GpsLocation?>

    /**
     * Configuration
     */
    fun updateGpsInterval(minutes: Int)
    fun updateSyncInterval(minutes: Int)
    fun getCurrentConfig(): GpsConfig
}