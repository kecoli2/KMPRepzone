package com.repzone.domain.model

import com.repzone.domain.common.DomainException

/**
 * Sync Status
 * Sunucuya veri gönderme durumu
 */
sealed class SyncStatus {
    object Idle : SyncStatus()
    object Syncing : SyncStatus()
    data class Success(val syncedCount: Int, val timestamp: Long) : SyncStatus()
    data class Failed(val error: DomainException, val retryCount: Int = 0) : SyncStatus()
    data class PartialSuccess(val successCount: Int, val failedCount: Int) : SyncStatus()
}

/**
 * Sync Result
 * Senkronizasyon sonucu detayları
 */
data class SyncResult(
    val totalCount: Int,
    val successCount: Int,
    val failedCount: Int,
    val timestamp: Long,
    val successfulIds: List<String> = emptyList(),
    val failedIds: List<String> = emptyList(),
    val error: DomainException? = null
) {
    val isSuccessful: Boolean get() = failedCount == 0
    val isPartialSuccess: Boolean get() = successCount > 0 && failedCount > 0
}