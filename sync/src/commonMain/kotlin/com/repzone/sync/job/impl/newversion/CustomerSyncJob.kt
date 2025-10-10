package com.repzone.sync.job.impl.newversion

import com.repzone.core.constant.ICustomerApiControllerConstant
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CustomerDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class CustomerSyncJob(apiService: ISyncApiService<List<CustomerDto>>,
                      bulkInsertService: IBulkInsertService<List<CustomerDto>>,
                      syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CustomerDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.CUSTOMERS
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_LIST_ENDPOINT
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count customer..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count product customer saved..."
    }

    override fun extractLastId(dtoData: List<CustomerDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<CustomerDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}