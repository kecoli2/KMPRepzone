package com.repzone.sync.job.impl

import com.repzone.core.constant.ICustomerApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CrmPriceListParameterDto
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
import repzonemobile.core.generated.resources.job_customer_price

class CustomerGroupPriceParametersSyncJob(apiService: ISyncApiService<List<CrmPriceListParameterDto>>,
                                          bulkInsertService: IBulkInsertService<List<CrmPriceListParameterDto>>,
                                          syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<CrmPriceListParameterDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.CUSTOMERS_GROUP_PRICE
    override val defaultRequestEndPoint = ICustomerApiControllerConstant.CUSTOMER_GROUP_PRICES_PARAMETERS_ENDPOINT
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
            args = listOf(count, Res.string.job_customer_price)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_customer_price)
        )
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