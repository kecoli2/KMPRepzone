package com.repzone.sync.manager

import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.SyncProgress
import com.repzone.sync.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit

class SyncManagerImpl(private val syncJobs: Map<SyncJobType, ISyncJob>, private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)): ISyncManager {
    //region Field
    private val mutex = Mutex()
    private val runningJobs = mutableMapOf<SyncJobType, Job>()
    private val jobHistory = MutableSharedFlow<SyncJobResult>(replay = 50)
    private var isPaused = false

    private val jobSemaphore = Semaphore(5) // AYNI ANDA CALISACAK JOB SAYISI
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override val allJobsStatus: Flow<Map<SyncJobType, SyncJobStatus>> =
        combine(syncJobs.map { (type, job) ->
            job.statusFlow.map { status -> type to status }
        }) { statusPairs ->
            statusPairs.toMap()
        }

    override val overallProgress: Flow<SyncProgress> =
        allJobsStatus.map { statusMap ->
            val totalJobs = statusMap.size
            val completedJobs = statusMap.count { (_, status) -> status is SyncJobStatus.Success }
            val runningJobs = statusMap.count { (_, status) ->
                status is SyncJobStatus.Running || status is SyncJobStatus.Progress
            }
            val failedJobs = statusMap.count { (_, status) -> status is SyncJobStatus.Failed }
            val isRunning = runningJobs > 0

            SyncProgress(totalJobs, completedJobs, runningJobs, failedJobs, isRunning)
        }

    override suspend fun startSync(userRole: UserRole) {
        val applicableJobs = syncJobs.filter { (_, job) ->
            job.isApplicableForRole(userRole)
        }.keys.toList()

        startSpecificJobs(applicableJobs)
    }

    override suspend fun startSpecificJobs(jobs: List<SyncJobType>) = mutex.withLock {
        if (isPaused) return@withLock

        jobs.forEach { jobType ->
            if (jobType !in runningJobs) {
                val job = syncJobs[jobType] ?: return@forEach

                val coroutineJob = scope.launch {
                    jobSemaphore.withPermit {
                        try {
                            val result = job.execute()
                            jobHistory.emit(result)
                        } catch (e: Exception) {
                            // Job kendi exception'larını handle ediyor
                        } finally {
                            mutex.withLock {
                                runningJobs.remove(jobType)
                            }
                        }
                    }
                }

                runningJobs[jobType] = coroutineJob
            }
        }
    }

    override suspend fun pauseAll() = mutex.withLock {
        isPaused = true
        // Mevcut job'ları durdurmuyoruz, sadece yeni başlatılmasını engelliyoruz
    }

    override suspend fun resumeAll() = mutex.withLock {
        isPaused = false
    }

    override suspend fun cancelAll() = mutex.withLock {
        isPaused = true

        // Tüm running job'ları iptal et
        runningJobs.values.forEach { job ->
            job.cancel()
        }

        // Sync job'larına da cancel sinyali gönder
        syncJobs.values.forEach { syncJob ->
            syncJob.cancel()
        }

        runningJobs.clear()
    }

    override fun getJobStatus(jobType: SyncJobType): Flow<SyncJobStatus> =
        syncJobs[jobType]?.statusFlow ?: flowOf(SyncJobStatus.Failed("Job not found", false))

    override fun getJobHistory(): Flow<List<SyncJobResult>> =
        jobHistory.asSharedFlow()
            .scan(emptyList()) { acc, result ->
                (acc + result).takeLast(50) // Son 50 sonucu tut
            }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}