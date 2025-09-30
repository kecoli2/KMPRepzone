package com.repzone.sync.interfaces

import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import kotlinx.coroutines.flow.Flow

interface ISyncJob {
    val jobType: SyncJobType
    val statusFlow: Flow<SyncJobStatus>

    suspend fun execute(): SyncJobResult
    suspend fun cancel()
    fun isApplicableForRole(userRole: com.repzone.sync.model.UserRole): Boolean
}