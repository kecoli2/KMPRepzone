package com.repzone.sync.job.impl

import com.repzone.core.constant.IMiscApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.SyncPaymentPlanDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobType
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class PaymentSyncJob(apiService: ISyncApiService<List<SyncPaymentPlanDto>>,
                     bulkinsertService: IBulkInsertService<List<SyncPaymentPlanDto>>,
                     syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<SyncPaymentPlanDto>>(apiService, bulkinsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.PAYMENT_PLAN
    override val defaultRequestEndPoint = IMiscApiControllerConstant.PAYMENT_PLAN_LIST_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.NONE
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_payment_plan)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_payment_plan)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<SyncPaymentPlanDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<SyncPaymentPlanDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        return value
    }
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
}