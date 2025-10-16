package com.repzone.core.ui.ui.sync

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.SyncProgress
import com.repzone.sync.transaction.TransactionStats

data class SyncUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val allJobsStatus: Map<SyncJobType, SyncJobStatus> = emptyMap(),
    val overallProgress: SyncProgress = SyncProgress(0, 0, 0, 0, false),
    val coordinatorStats: TransactionStats = TransactionStats(0, 0, 0, 0),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val allJobsStatusList: List<Pair<SyncJobType, SyncJobStatus>> = emptyList()
) : HasUiFrame {
    override fun copyWithUiFrame(newUiFrame: UiFrame): SyncUiState {
        return copy(uiFrame = uiFrame)
    }
}