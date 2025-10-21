package com.repzone.sync.interfaces

import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.SyncProgress
import com.repzone.core.enums.UserRole
import kotlinx.coroutines.flow.Flow

interface ISyncManager {
    val allJobsStatus: Flow<Map<SyncJobType, SyncJobStatus>>
    val overallProgress: Flow<SyncProgress>

    suspend fun startSync()
    suspend fun startSpecificJobs(jobs: List<SyncJobType>)
    suspend fun pauseAll()
    suspend fun resumeAll()
    suspend fun cancelAll()

    fun getJobStatus(jobType: SyncJobType): Flow<SyncJobStatus>
    fun getJobHistory(): Flow<List<SyncJobResult>>
}