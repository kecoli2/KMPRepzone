package com.repzone.core.ui.viewmodel.sync

import com.repzone.core.enums.UserRole
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.ui.sync.SyncUiState
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.transaction.TransactionCoordinator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncViewModel(private val syncManager: ISyncManager, private val iusereSession: IUserSession, private val transactionCoordinator: TransactionCoordinator,)
        : BaseViewModel<SyncUiState, SyncViewModel.Event>(SyncUiState()) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
      init {
        scope.launch {
            syncManager.allJobsStatus.collect { statusMap ->
                updateState {
                    it.copy(
                        allJobsStatus = statusMap,
                        allJobsStatusList = statusMap.entries.map { entry ->
                            entry.key to entry.value
                        }
                    )
                }
            }
        }
        scope.launch {
            while (true) {
                try {
                    val stats = transactionCoordinator.getStats()
                    updateState { it.copy(coordinatorStats = stats) }
                } catch (e: Exception) {
                    // Continue on error
                }
                delay(2000)
            }
        }
        startAllJobs()
      }
    //endregion

    //region Public Method
    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        transactionCoordinator.shutdown()
    }

    override fun onDispose() {
        super.onDispose()
        transactionCoordinator.shutdown()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun getCurrentUserRole(): UserRole {
        return iusereSession.getActiveSession()?.identity?.role ?: UserRole.SALES_REP
    }

    private fun startAllJobs() {
        scope.launch {
            try {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = true),
                        errorMessage = null,
                        successMessage = null
                    )
                }

                val userRole = getCurrentUserRole()
                syncManager.startSync(userRole)

            } catch (e: Exception) {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false, error = e.message),
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }
    //endregion

    //region Event
     sealed class Event{
        object Success: Event()
     }
    //region Event
}