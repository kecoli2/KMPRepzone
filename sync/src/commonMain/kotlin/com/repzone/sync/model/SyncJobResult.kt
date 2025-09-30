package com.repzone.sync.model

data class SyncJobResult(
    val jobType: SyncJobType,
    val status: SyncJobStatus,
    val startTime: Long,
    val endTime: Long? = null,
    val recordsProcessed: Int = 0,
    val errors: List<String> = emptyList()
)