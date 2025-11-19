package com.repzone.core.ui.viewmodel.sync

import com.repzone.core.enums.UserRole
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.ui.sync.SyncUiState
import com.repzone.core.ui.viewmodel.splash.SplashScreenViewModel.SplashScreenOperation
import com.repzone.domain.common.DomainException
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.transaction.TransactionCoordinator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.repzone.domain.common.Result
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.manager.gps.IGpsTrackingManager

class SyncViewModel(private val syncManager: ISyncManager,
                    private val iusereSession: IUserSession,
                    private val transactionCoordinator: TransactionCoordinator,
                    private val gpsTrackingManager: IGpsTrackingManager)
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
    suspend fun onEvent(event: Event): Result<Unit> {
        return try {
            when(event){
                Event.Success -> {
                    gpsTrackingManager.initialize()
                    gpsTrackingManager.start()
                        .onSuccess {
                            it
                        }.onError {
                            it
                        }
                }
            }
        }catch (e: Exception){
            Result.Error(DomainException.UnknownException(cause = e))
        }
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
                syncManager.startSync()

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