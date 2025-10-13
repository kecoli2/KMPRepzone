package com.repzone.sync.job.impl

import com.repzone.core.constant.ICustomerApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class CustomerGroupSyncJob(api: ISyncApiService<List<CustomerGroupDto>>,
                           bulkInsert: IBulkInsertService<List<CustomerGroupDto>>,
                           private val syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CustomerGroupDto>>(api, bulkInsert, syncModuleRepository)   {
    //region Field
    override val allowedRoles = setOf(UserRole.ADMIN, UserRole.SALES_REP)
    override val jobType = SyncJobType.CUSTOMERS_GROUP
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_GROUPS_ENDPOINT
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count customer group..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count customer group saved..."
    }

    override fun extractLastId(dtoData: List<CustomerGroupDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
    }

    override fun getDataSize(dtoData: List<CustomerGroupDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 250
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}