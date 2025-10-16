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
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_email

class CustomerEmailSyncJob(apiService: ISyncApiService<List<CustomerEmailDto>>,
                           bulkinsertService: IBulkInsertService<List<CustomerEmailDto>>,
                           syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CustomerEmailDto>>(apiService, bulkinsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.CUSTOMERS_EMAIL
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_EMAIL_ENDPOINT
    override val moduleType = UIModule.NEW
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

    override fun extractLastId(dtoData: List<CustomerEmailDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
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