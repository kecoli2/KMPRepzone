package com.repzone.sync.interfaces

import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import kotlinx.coroutines.flow.Flow

interface ISyncJob {
    val jobType: SyncJobType
    val jobGroup: SyncJobGroup
    val moduleType : UIModule
    val statusFlow: Flow<SyncJobStatus>

    suspend fun execute(): SyncJobResult
    suspend fun cancel()
    fun isApplicableForRole(userRole: UserRole): Boolean
}