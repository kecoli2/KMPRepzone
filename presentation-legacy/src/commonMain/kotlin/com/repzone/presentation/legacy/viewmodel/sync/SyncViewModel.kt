package com.repzone.presentation.legacy.viewmodel.sync

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.SyncJobType.*
import com.repzone.sync.model.SyncProgress
import com.repzone.core.enums.UserRole
import com.repzone.core.interfaces.IUserSession
import com.repzone.sync.transaction.TransactionCoordinator
import com.repzone.sync.transaction.TransactionStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Sync Test ViewModel - BaseViewModel pattern kullanır
 */
class SyncTestViewModel(private val syncManager: ISyncManager,
                        private val transactionCoordinator: TransactionCoordinator,
                        private val userSession: IUserSession
) : BaseViewModel<SyncTestViewModel.State, SyncTestViewModel.Event>(
    initialState = State()
) {

    /**
     * View State - HasUiFrame implement eder
     */
    data class State(
        override val uiFrame: UiFrame = UiFrame(),
        val allJobsStatus: Map<SyncJobType, SyncJobStatus> = emptyMap(),
        val overallProgress: SyncProgress = SyncProgress(0, 0, 0, 0, false),
        val userRole: UserRole = UserRole.SALES_REP,
        val coordinatorStats: TransactionStats = TransactionStats(0, 0, 0, 0),
        val successMessage: String? = null,
        val errorMessage: String? = null,
        val jobHistory: List<SyncJobResult> = emptyList(),
        val allJobsStatusList: List<Pair<SyncJobType, SyncJobStatus>> = emptyList()
    ) : HasUiFrame {
        override fun copyWithUiFrame(newUiFrame: UiFrame): State {
            return copy(uiFrame = uiFrame)
        }
    }

    /**
     * View Events - UI'dan gelen action'lar
     */
    sealed class Event {
        data object StartFullSync : Event()
        data class StartSpecificJob(val jobType: SyncJobType) : Event()
        data class StartMultipleJobs(val jobs: List<SyncJobType>) : Event()
        data object PauseAll : Event()
        data object ResumeAll : Event()
        data object CancelAll : Event()
        data object RefreshStats : Event()
        data object ClearSuccess : Event()
        data object ClearError : Event()
    }

    init {
        // Job statuses'ı dinle
        // Job statuses'ı dinle
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

        // Overall progress'i dinle
        scope.launch {
            syncManager.overallProgress.collect { progress ->
                updateState { it.copy(overallProgress = progress) }
            }
        }

        // Job history'yi dinle
        scope.launch {
            syncManager.getJobHistory().collect { history ->
                updateState { it.copy(jobHistory = history) }
            }
        }

        // User role'ü periyodik güncelle
        scope.launch {
            while (true) {
                val role = getCurrentUserRole()
                updateState { it.copy(userRole = role) }
                delay(5000)
            }
        }

        // Coordinator stats'ı periyodik güncelle
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
    }

    override fun onStart() {
        super.onStart()
        // İlk stats yükle
        scope.launch {
            try {
                val stats = transactionCoordinator.getStats()
                updateState { it.copy(coordinatorStats = stats) }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    override fun onStop() {
        super.onStop()
        transactionCoordinator.shutdown()
    }

    override fun onDispose() {
        super.onDispose()
        transactionCoordinator.shutdown()
    }

    private fun getCurrentUserRole(): UserRole {
        return userSession.getActiveSession()?.identity?.role ?: UserRole.SALES_REP
    }

    /**
     * Event handler
     */
    fun onEvent(event: Event) {
        when (event) {
            is Event.StartFullSync -> startFullSync()
            is Event.StartSpecificJob -> startSpecificJob(event.jobType)
            is Event.StartMultipleJobs -> startMultipleJobs(event.jobs)
            is Event.PauseAll -> pauseAll()
            is Event.ResumeAll -> resumeAll()
            is Event.CancelAll -> cancelAll()
            is Event.RefreshStats -> refreshStats()
            is Event.ClearSuccess -> updateState { it.copy(successMessage = null) }
            is Event.ClearError -> updateState { it.copy(errorMessage = null) }
        }
    }

    private fun startFullSync() {
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

                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        successMessage = "✅ ${userRole.getDisplayName()} rolü için sync başlatıldı!"
                    )
                }

            } catch (e: Exception) {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        errorMessage = "Sync başlatılamadı: ${e.message}"
                    )
                }
            }
        }
    }

    private fun startSpecificJob(jobType: SyncJobType) {
        scope.launch {
            try {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = true, error = null),
                        successMessage = null
                    )
                }

                syncManager.startSpecificJobs(listOf(jobType))

                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        successMessage = "✅ ${jobType.getDisplayName()} sync başlatıldı!"
                    )
                }

            } catch (e: Exception) {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        errorMessage = "❌ ${jobType.getDisplayName()} sync başlatılamadı: ${e.message}" // ← Değişti
                    )
                }
            }
        }
    }

    private fun startMultipleJobs(jobs: List<SyncJobType>) {
        scope.launch {
            try {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = true, error = null),
                        successMessage = null
                    )
                }

                syncManager.startSpecificJobs(jobs)

                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        successMessage = "✅ ${jobs.size} sync işlemi başlatıldı!"
                    )
                }

            } catch (e: Exception) {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(isLoading = false),
                        errorMessage = "Sync işlemleri başlatılamadı: ${e.message}" // ← Değişti
                    )
                }
            }
        }
    }

    private fun pauseAll() {
        scope.launch {
            try {
                syncManager.pauseAll()
                updateState { it.copy(successMessage = "⏸️ Tüm sync işlemleri duraklatıldı") }
            } catch (e: Exception) {
                updateState { it.copy(errorMessage = "❌ Duraklatma başarısız: ${e.message}") }
            }
        }
    }

    private fun resumeAll() {
        scope.launch {
            try {
                syncManager.resumeAll()
                updateState { it.copy(successMessage = "▶️ Sync işlemleri devam ettiriliyor") }
            } catch (e: Exception) {
                updateState { it.copy(errorMessage = "❌ Devam ettirme başarısız: ${e.message}") }
            }
        }
    }

    private fun cancelAll() {
        scope.launch {
            try {
                syncManager.cancelAll()
                updateState { it.copy(successMessage = "Tüm sync işlemleri iptal edildi") }
            } catch (e: Exception) {
                updateState { it.copy(errorMessage = "İptal etme başarısız: ${e.message}") }
            }
        }
    }

    private fun refreshStats() {
        scope.launch {
            try {
                val stats = transactionCoordinator.getStats()
                updateState { it.copy(
                    coordinatorStats = stats,
                    successMessage = "🔄 İstatistikler güncellendi"
                )}
            } catch (e: Exception) {
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = currentState.uiFrame.copy(error = "İstatistik güncelleme başarısız: ${e.message}")
                    )
                }
            }
        }
    }
}

// Extension functions
private fun UserRole.getDisplayName(): String = when (this) {
    UserRole.SALES_REP -> "Satış Temsilcisi"
    UserRole.DISTRIBUTION -> "Dağıtıcı Elemanı"
}

private fun SyncJobType.getDisplayName(): String = when (this) {
    PRODUCTS -> "Ürünler"
    CUSTOMERS -> "Müşteriler"
    PRODUCTS_GROUP -> "Ürün Grupları"
    ROUTE -> "Rotalar"
    CUSTOMERS_GROUP -> "Müşteri Grupları"
    TABLE_REPLICATION -> "Replikasyon"
    COMMON_MODULES -> "Genel Görev"
    FORM -> "Formlar"
    EXTRA_TABLE -> "Extarnal"
    STOCK -> "Stoklar"
    CUSTOMERS_EMAIL -> "Müşteri Email"
    CUSTOMERS_PRICE_PARAMETERS -> "Müşteri Fiyat"
    CUSTOMERS_GROUP_PRICE -> "Müşteri Grubu Fiyatı"
    COMMON_MODULES_REASONS -> "Genel Görev Reasons"
    COMMON_DOCUMENT_MAPS -> "Document Görevi"
    COMMON_DYNAMIC_PAGES -> "Dynamic Pages Görevi"
}