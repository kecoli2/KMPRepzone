package com.repzone.sync.job.base

import com.repzone.core.util.toJson
import com.repzone.core.util.toModel
import com.repzone.core.util.toModelOrDefault
import com.repzone.domain.model.RequestType
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseSyncJob(private val iSyncModuleRepository: ISyncModuleRepository) : ISyncJob {
    //region Field
    private val _statusFlow = MutableStateFlow<SyncJobStatus>(SyncJobStatus.Idle)
    override val statusFlow: StateFlow<SyncJobStatus> = _statusFlow.asStateFlow()
    private var isCancelled = false
    private var requestFilter : FilterModelRequest? = null
    private var syncModuleModel : SyncModuleModel? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    fun getSyncModuleModel(): SyncModuleModel? {
        return syncModuleModel
    }

    override suspend fun execute(): SyncJobResult {
        val startTime = getTimeMillis()
        var recordsProcessed = 0
        val errors = mutableListOf<String>()

        return try {
            updateStatus(SyncJobStatus.Running)
            syncModuleModel = iSyncModuleRepository.getById(jobType.ordinal.toLong())
            if(syncModuleModel == null){
                requestFilter = onPreExecuteFilterModel(FilterModelRequest())
                syncModuleModel = SyncModuleModel(jobType.ordinal.toLong(), defaultRequestEndPoint,requestFilter.toJson(),null,
                    RequestType.POST)

            }else{
                requestFilter?.fetchOnlyActive = syncModuleModel?.lastSyncDate != null
                requestFilter = onPreExecuteFilterModel(syncModuleModel!!.requestFilter!!.toModel<FilterModelRequest>()!!)
            }
            recordsProcessed = executeSync()
            val endTime = getTimeMillis()
            val duration = endTime - startTime

            val successStatus = SyncJobStatus.Success(recordsProcessed, duration)
            updateStatus(successStatus)
            syncModuleModel?.requestFilter = requestFilter.toJson()
            syncModuleModel?.lastSyncDate = endTime
            iSyncModuleRepository.upsert(syncModuleModel!!)
            SyncJobResult(
                jobType = jobType,
                status = successStatus,
                startTime = startTime,
                endTime = endTime,
                recordsProcessed = recordsProcessed
            )


        } catch (e: CancellationException) {
            val cancelledStatus = SyncJobStatus.Failed("Job cancelled", retryable = false)
            updateStatus(cancelledStatus)

            SyncJobResult(
                jobType = jobType,
                status = cancelledStatus,
                startTime = startTime,
                endTime = getTimeMillis(),
                recordsProcessed = recordsProcessed,
                errors = listOf("Cancelled by user")
            )
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            errors.add(errorMessage)

            val failedStatus = SyncJobStatus.Failed(errorMessage, retryable = true)
            updateStatus(failedStatus)

            SyncJobResult(
                jobType = jobType,
                status = failedStatus,
                startTime = startTime,
                endTime = getTimeMillis(),
                recordsProcessed = recordsProcessed,
                errors = errors
            )
        }
    }

    override suspend fun cancel() {
        isCancelled = true
        updateStatus(SyncJobStatus.Failed("Cancelled", retryable = false))
    }
    //endregion

    //region Protected Method
    protected fun checkCancellation() {
        if (isCancelled) {
            throw CancellationException("Job cancelled")
        }
    }
    protected fun updateProgress(current: Int, total: Int, message: String? = null) {
        updateStatus(SyncJobStatus.Progress(current, total, message))
    }

    /**
     * Alt sınıflar bu metodu implement eder
     * Alt joblarda bs ler buraya koruz Haluk ile gorusecegiz
     * @return İşlenen kayıt sayısı
     */
    protected abstract suspend fun executeSync(): Int
    protected abstract fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest
    protected fun updateStatus(status: SyncJobStatus) {
        _statusFlow.value = status
    }
    protected abstract val defaultRequestEndPoint: String


    //endregion

    //region Private Method
    private fun updateSyncModule(){

    }
    //endregion
}