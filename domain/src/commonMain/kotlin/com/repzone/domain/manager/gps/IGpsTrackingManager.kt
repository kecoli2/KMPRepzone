package com.repzone.domain.manager.gps

import com.repzone.core.util.PermissionStatus
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.LocationMetadata
import com.repzone.domain.model.gps.ServiceState
import kotlinx.coroutines.flow.Flow
import com.repzone.domain.common.Result
import com.repzone.domain.model.SyncResult
import com.repzone.domain.model.SyncStatus

/**
 * Tüm servisleri koordine eden ana interface
 */
interface IGpsTrackingManager {
    suspend fun initialize(config: GpsConfig): Result<Unit>
    suspend fun start(): Result<Unit>
    suspend fun stop(): Result<Unit>
    suspend fun pause(): Result<Unit>
    suspend fun resume(): Result<Unit>
    suspend fun forceGpsUpdate(): Result<GpsLocation>
    suspend fun forceSyncNow(): Result<SyncResult>
    suspend fun getLocationMetadata(): LocationMetadata
    suspend fun getLastLocation(): GpsLocation?
    fun getCurrentConfig(): GpsConfig
    suspend fun updateGpsInterval(minutes: Int): Result<Unit>
    suspend fun updateSyncInterval(minutes: Int): Result<Unit>
    fun observeServiceState(): Flow<ServiceState>
    fun observeLastLocation(): Flow<GpsLocation?>
    fun observeSyncStatus(): Flow<SyncStatus>
    suspend fun checkPermissions(): PermissionStatus
    suspend fun requestPermissions(): PermissionStatus
}