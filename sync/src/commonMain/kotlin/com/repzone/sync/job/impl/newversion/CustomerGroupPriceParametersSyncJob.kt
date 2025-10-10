package com.repzone.sync.job.impl.newversion

import com.repzone.core.constant.ICustomerApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class CustomerGroupPriceParametersSyncJob(apiService: ISyncApiService<List<CrmPriceListParameterDto>>,
                                          bulkInsertService: IBulkInsertService<List<CrmPriceListParameterDto>>,
                                          syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CrmPriceListParameterDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.CUSTOMERS_GROUP_PRICE
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_GROUP_PRICES_PARAMETERS_ENDPOINT
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count customer group price parameters..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count product customer group price parameters saved..."
    }

    override fun extractLastId(dtoData: List<CrmPriceListParameterDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<CrmPriceListParameterDto>): Int {
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