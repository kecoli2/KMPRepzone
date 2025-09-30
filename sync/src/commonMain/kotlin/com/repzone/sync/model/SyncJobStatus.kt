package com.repzone.sync.model

sealed class SyncJobStatus {
    data object Idle : SyncJobStatus()
    data object Running : SyncJobStatus()
    data class Progress(val current: Int, val total: Int, val message: String? = null) : SyncJobStatus()
    data class Success(val recordCount: Int, val duration: Long) : SyncJobStatus()
    data class Failed(val error: String, val retryable: Boolean = true) : SyncJobStatus()
}