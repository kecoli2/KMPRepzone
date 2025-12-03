package com.repzone.sync.job.impl

import com.repzone.core.constant.IDistributionApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.SyncCustomerProductDistributionDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobType
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_customer_distribution
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CustomerProductDistributionsSyncJob(apiService: ISyncApiService<List<SyncCustomerProductDistributionDto>>,
                                          bulkinsertService: IBulkInsertService<List<SyncCustomerProductDistributionDto>>,
                                          syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<SyncCustomerProductDistributionDto>>(apiService, bulkinsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.DISTRIBUTIONS_CUSTOMER_PRODUCT
    override val defaultRequestEndPoint = IDistributionApiControllerConstant.CUSTOMER_PRODUCT_DISTRIBUTION_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.DISTRIBUTIONS
    //endregion Field

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_customer_distribution)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_customer_distribution)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<SyncCustomerProductDistributionDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<SyncCustomerProductDistributionDto>): Int {
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