package com.repzone.data.service

import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.domain.common.DomainException
import com.repzone.domain.model.SyncStatus
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.service.IGpsDataSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import com.repzone.domain.common.Result
import com.repzone.domain.common.onError
import com.repzone.domain.model.SyncResult
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.util.retryWithBackoff
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 *
 *  Özellikler:
 *
 * Batch processing (50 items/batch)
 * Retry mechanism (retryWithBackoff)
 * Rate limiting (100ms delay)
 * Mock API client (TODO: Ktor)
 * Ayrıca Firebase Real Database
 *
 *  İşleyiş:
 *
 * syncToServer() çağrılır
 * Unsynced location'lar alınır
 * 50'şerli batch'lere bölünür
 * Her batch için syncBatch() çalışır (retry ile)
 * Başarılı olanlar markAsSynced() ile işaretlenir
 */

class GpsDataSyncServiceImpl(private val locationRepository: ILocationRepository,
                             private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)):
    IGpsDataSyncService {
    //region Field
    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    private var lastSyncTime: Long? = null
    private var isSyncing = false
    //endregion

    //region Properties
    //endregion

    //region Public Method
    override suspend fun syncToServer(): Result<SyncResult> {
        return withContext(Dispatchers.Default) {
            try {
                if (isSyncing) {
                    return@withContext Result.Error(DomainException.UnknownException(cause = IllegalStateException("Sync already in progress")) )
                }

                isSyncing = true
                _syncStatus.value = SyncStatus.Syncing

                // Senkronize edilmemiş konumları al
                val unsyncedLocations = locationRepository.getUnsyncedLocations()

                if (unsyncedLocations.isEmpty()) {
                    _syncStatus.value = SyncStatus.Success(0, now())
                    lastSyncTime = now()
                    isSyncing = false
                    return@withContext Result.Success(
                        SyncResult(
                            totalCount = 0,
                            successCount = 0,
                            failedCount = 0,
                            timestamp = now()
                        )
                    )
                }

                // Verileri sunucuya gönder
                val result = syncLocations(unsyncedLocations).getOrThrow()

                _syncStatus.value = if (result.isSuccessful) {
                    SyncStatus.Success(result.successCount, now())
                } else if (result.isPartialSuccess) {
                    SyncStatus.PartialSuccess(result.successCount, result.failedCount)
                } else {
                    SyncStatus.Failed(result.error ?: DomainException.UnknownException(cause = Exception("Sync failed")))
                }
                lastSyncTime = now()
                isSyncing = false
                Result.Success(result)
            } catch (e: Exception) {
                _syncStatus.value = SyncStatus.Failed(DomainException.UnknownException(cause = e))
                isSyncing = false
                Result.Error(DomainException.UnknownException(cause = e))
            }
        }
    }

    override suspend fun syncLocations(locations: List<GpsLocation>): Result<SyncResult> {
        return withContext(Dispatchers.Default) {
            try {
                if (locations.isEmpty()) {
                    return@withContext Result.Success(
                        SyncResult(
                            totalCount = 0,
                            successCount = 0,
                            failedCount = 0,
                            timestamp = now()
                        )
                    )
                }

                // Batch processing (her seferinde max 50 kayıt)
                val batchSize = 50
                val batches = locations.chunked(batchSize)

                var totalSuccess = 0
                var totalFailed = 0
                val syncedIds = mutableListOf<String>()

                for (batch in batches) {
                    val batchResult = syncBatch(batch)

                    if (batchResult.isSuccess) {
                        val successCount = batchResult.getOrNull() ?: 0
                        totalSuccess += successCount

                        // Başarılı olanları işaretle
                        syncedIds.addAll(batch.map { it.id })
                    } else {
                        totalFailed += batch.size
                    }

                    // Rate limiting için kısa bir bekleme
                    delay(100)
                }

                // Başarılı konumları işaretle
                if (syncedIds.isNotEmpty()) {
                    locationRepository.markAsSynced(syncedIds)
                }

                val result = SyncResult(
                    totalCount = locations.size,
                    successCount = totalSuccess,
                    failedCount = totalFailed,
                    timestamp = now(),
                    error = if (totalFailed > 0) DomainException.UnknownException(cause = Exception("Aynı lokasyon kaydı gönderilmedi")) else null
                )

                Result.Success(result)

            } catch (e: Exception) {
                Result.Error(DomainException.UnknownException(cause = e))
            }
        }
    }

    override suspend fun getPendingDataCount(): Int {
        return locationRepository.getUnsyncedLocations().size
    }

    override suspend fun clearSyncedData(): Result<Int> {
        return try {
            val allLocations = locationRepository.getLocationHistory(Int.MAX_VALUE)
            val syncedLocations = allLocations.filter { it.isSynced }

            if (syncedLocations.isEmpty()) {
                return Result.Success(0)
            }

            // Senkronize edilmiş ve eski olan verileri sil (7 günden eski)
            val cutoffTime = now() - (7 * 24 * 60 * 60 * 1000L)
            val oldSyncedLocations = syncedLocations.filter {
                it.timestamp < cutoffTime
            }

            if (oldSyncedLocations.isEmpty()) {
                return Result.Success(0)
            }

            locationRepository.clearOldLocations(cutoffTime)

        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override fun observeSyncStatus(): Flow<SyncStatus> {
        return _syncStatus.asStateFlow()
    }

    override suspend fun getLastSyncTime(): Long? {
        return lastSyncTime
    }
    //endregion

    //region Private Method
    private suspend fun syncBatch(locations: List<GpsLocation>): Result<Int> {
        return retryWithBackoff(times = 3, initialDelayMillis = 1000) {
            // API'ye gönder
            //apiClient.sendLocations(locations)
            // SONRASINDA BURAYA SIGNALR CONNECTION DA GELEBILIR AYRICA BURADA FIREBASE ENTEGRASYONUDA YAPACAGIZ

            locations.size
        }
    }

    suspend fun syncWithRetry(maxRetries: Int = 3): Result<SyncResult> {
        var lastError: Throwable? = null

        repeat(maxRetries) { attempt ->
            val result = syncToServer()
            if (result.isSuccess) {
                return result
            }
            lastError = result.getOrThrow().error

            // Son denemeden sonra bekleme
            if (attempt < maxRetries - 1) {
                delay(2000L * (attempt + 1))
            }
        }
        if(lastError != null){
            return Result.Error(DomainException.UnknownException(cause = lastError) )
        }
        return Result.Error(DomainException.UnknownException(cause = Exception("Sync failed after $maxRetries retries")) )
    }
    //endregion
}