package com.repzone.sync.job.impl

import com.repzone.core.constant.ICustomerApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.selectAsFlow
import com.repzone.database.runtime.selectFirstAsFlow
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.sync.model.SyncJobGroup
import kotlinx.coroutines.runBlocking
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_email
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CustomerEmailSyncJob(apiService: ISyncApiService<List<CustomerEmailDto>>,
                           bulkinsertService: IBulkInsertService<List<CustomerEmailDto>>,
                           syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CustomerEmailDto>>(apiService, bulkinsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.CUSTOMERS_EMAIL
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_EMAIL_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.CUSTOMER
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_email)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_email)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<CustomerEmailDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<CustomerEmailDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 250
        return value
    }
    //endregion

}