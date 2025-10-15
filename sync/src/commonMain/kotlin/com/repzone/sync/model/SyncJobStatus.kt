package com.repzone.sync.model

import com.repzone.core.model.ResourceUI

sealed class SyncJobStatus {
    data object Idle : SyncJobStatus()
    data object Running : SyncJobStatus()
    data class Progress(val current: Int, val total: Int, val resourceUi: ResourceUI? = null) : SyncJobStatus()
    data class Success(val recordCount: Int, val resourceUi: ResourceUI? = null, val duration: Long) : SyncJobStatus()
    data class Failed(val error: String, val retryable: Boolean = true) : SyncJobStatus()
}