package com.repzone.data.service

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
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.domain.model.SyncResult
import com.repzone.domain.model.gps.GpsLocation
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
                             private val iFirebaseRealtimeDatabase: IFirebaseRealtimeDatabase,
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
        return withContext(coroutineScope.coroutineContext) {
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
        return withContext(coroutineScope.coroutineContext) {
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
                        val successList = batchResult.getOrNull()
                        successList?.let { it ->
                            totalSuccess += it.successCount
                        }

                        syncedIds.addAll(successList?.successfulIds ?: emptyList())
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
    private suspend fun syncBatch(locations: List<GpsLocation>): Result<SyncResult> {
        val successfulIds = mutableSetOf<String>()
        val failedIds = mutableSetOf<String>()

        repeat(3) { attempt ->
            // Sadece başarısız olanları tekrar dene
            val locationsToTry = if (attempt == 0) {
                locations
            } else {
                locations.filter { it.id in failedIds }
            }

            if (locationsToTry.isEmpty()) {
                return@repeat // Hepsi başarılı
            }

            locationsToTry.forEach { location ->
                val success = iFirebaseRealtimeDatabase.sendToFirebase(location)

                if (success.isSuccess) {
                    successfulIds.add(location.id)
                    failedIds.remove(location.id)
                } else {
                    failedIds.add(location.id)
                }
            }

            // Başarısız varsa ve son deneme değilse bekle
            if (failedIds.isNotEmpty() && attempt < 2) {
                delay(1000L * (attempt + 1)) // Exponential backoff
            }
        }

        return Result.Success(SyncResult(
            totalCount = locations.size,
            successCount = successfulIds.size,
            failedCount = failedIds.size,
            successfulIds = successfulIds.toList(),
            failedIds = failedIds.toList(),
            timestamp = now()
        ))
    }
    //endregion
}