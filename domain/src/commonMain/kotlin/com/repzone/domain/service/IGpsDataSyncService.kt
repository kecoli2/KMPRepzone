package com.repzone.domain.service

import com.repzone.domain.model.SyncStatus
import com.repzone.domain.common.Result
import com.repzone.domain.model.SyncResult
import com.repzone.domain.model.gps.GpsLocation
import kotlinx.coroutines.flow.Flow

interface IGpsDataSyncService {
    suspend fun syncToServer(): Result<SyncResult>
    suspend fun syncLocations(locations: List<GpsLocation>): Result<SyncResult>
    suspend fun getPendingDataCount(): Int
    suspend fun clearSyncedData(): Result<Int>
    fun schedulePeriodicSync(intervalMinutes: Int)
    fun cancelScheduledSync()
    fun observeSyncStatus(): Flow<SyncStatus>
    suspend fun getLastSyncTime(): Long?
}