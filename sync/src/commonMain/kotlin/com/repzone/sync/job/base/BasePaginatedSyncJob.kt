package com.repzone.sync.job.base

import com.repzone.core.enums.UIModule
import com.repzone.core.util.extensions.toJson
import com.repzone.core.util.extensions.jsonToModel
import com.repzone.domain.model.RequestType
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.util.SyncConstant
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_email
import kotlin.coroutines.cancellation.CancellationException

abstract class BasePaginatedSyncJob<TDto : Any>(
    protected val apiService: ISyncApiService<TDto>,
    protected val bulkInsertService: IBulkInsertService<TDto>,
    private val syncModuleRepository: ISyncModuleRepository
) : ISyncJob {

    //region Fields
    private val _statusFlow = MutableStateFlow<SyncJobStatus>(SyncJobStatus.Idle)
    override val statusFlow: StateFlow<SyncJobStatus> = _statusFlow.asStateFlow()
    private var isCancelled = false
    private var requestFilter: FilterModelRequest? = null
    private var syncModuleModel: SyncModuleModel? = null
    //endregion

    //region Role Management
    protected abstract val allowedRoles: Set<UserRole>

    override fun isApplicableForRole(userRole: UserRole): Boolean {
        return userRole in allowedRoles
    }
    //endregion

    //region Abstract Properties
    abstract override val jobType: SyncJobType
    protected abstract val defaultRequestEndPoint: String
    abstract override val moduleType: UIModule

    abstract override val jobGroup: SyncJobGroup
    //endregion

    //region Configurable Properties
    open val defaultTakeCount: Int = SyncConstant.TAKEN_COUNT
    open val fetchingMessage: ResourceUI = ResourceUI(Res.string.job_email)

    protected open fun getFetchedMessage(count: Int): ResourceUI = ResourceUI(res = Res.string.job_email, args = listOf(count))
    protected open fun getCompletedMessage(count: Int): ResourceUI = ResourceUI(res = Res.string.job_email, args = listOf(count))
    //endregion

    //region Abstract Methods
    protected abstract fun extractLastIdAndLastDate(dtoData: TDto, requestModel: FilterModelRequest?)
    protected abstract fun getDataSize(dtoData: TDto): Int
    //endregion

    //region Public Methods
    fun getSyncModuleModel(): SyncModuleModel? {
        return syncModuleModel
    }

    override suspend fun execute(): SyncJobResult {
        val startTime = getTimeMillis()
        var recordsProcessed = 0
        val errors = mutableListOf<String>()

        return try {
            updateStatus(SyncJobStatus.Running)

            syncModuleModel = syncModuleRepository.getById(jobType.ordinal.toLong(), moduleType)
            if (syncModuleModel == null) {
                requestFilter = onPreExecuteFilterModel(FilterModelRequest())
                syncModuleModel = SyncModuleModel(
                    jobType.ordinal.toLong(),
                    defaultRequestEndPoint,
                    requestFilter.toJson(),
                    null,
                    RequestType.POST
                )
            } else {
                requestFilter?.fetchOnlyActive = syncModuleModel?.lastSyncDate != null
                requestFilter = onPreExecuteFilterModel(
                    syncModuleModel!!.requestFilter!!.jsonToModel<FilterModelRequest>()!!
                )
            }

            recordsProcessed = executeSync()
            val endTime = getTimeMillis()
            val duration = endTime - startTime

            val successStatus = SyncJobStatus.Success(recordsProcessed, getCompletedMessage(recordsProcessed) ,duration)
            updateStatus(successStatus)
            syncModuleModel?.requestFilter = requestFilter.toJson()
            syncModuleModel?.lastSyncDate = endTime
            syncModuleRepository.upsert(syncModuleModel!!)

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

    //region Protected Methods
    protected fun checkCancellation() {
        if (isCancelled) {
            throw CancellationException("Job cancelled")
        }
    }

    protected fun updateProgress(current: Int, total: Int, resourceUI: ResourceUI) {
        updateStatus(SyncJobStatus.Progress(current, total, resourceUI))
    }

    protected fun updateStatus(status: SyncJobStatus) {
        _statusFlow.value = status
    }

    protected open fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = defaultTakeCount
        return value
    }
    //endregion

    //region Core Sync Logic
    private suspend fun executeSync(): Int {
        updateProgress(0, 100, fetchingMessage)
        checkCancellation()

        var totalInserted = 0
        var totalFetched = 0
        val pageSize = requestFilter?.take ?: defaultTakeCount

        // fetchPage already loops internally and emits each page
        apiService.fetchPage(getSyncModuleModel()!!, pageSize).collect { result ->
            when (result) {
                is ApiResult.Error -> {
                    handleError(result)
                }
                is ApiResult.Loading -> {
                    handleLoading(result)
                }
                is ApiResult.Success -> {
                    val dtoData = result.data
                    val fetchedSize = getDataSize(dtoData)
                    totalFetched += fetchedSize

                    updateProgress(25, 100, getFetchedMessage(totalFetched))
                    checkCancellation()

                    val inserted = bulkInsertService.upsertBatch(dtoData)
                    extractLastIdAndLastDate(dtoData,requestFilter)

                    totalInserted += inserted
                    onBatchProcessed(totalFetched, totalInserted)
                }
            }
        }

        updateProgress(100, 100, getCompletedMessage(totalFetched))
        return totalInserted
    }
    //endregion

    //region Overridable Handlers
    protected open fun handleError(error: ApiResult.Error) {
        throw Exception("API Error: ${error.exception.message}")
    }

    protected open fun handleLoading(loading: ApiResult.Loading) {
        // Default: do nothing
    }

    protected open fun onBatchProcessed(totalFetched: Int, totalInserted: Int) {
        // Default: do nothing
    }
    //endregion
}