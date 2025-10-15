package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ProductDto
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
import repzonemobile.core.generated.resources.job_product_parameters

class ProductSyncJob(apiService: ISyncApiService<List<ProductDto>>, bulkInsertService: IBulkInsertService<List<ProductDto>>, syncModuleRepository: ISyncModuleRepository)
    : BasePaginatedSyncJob<List<ProductDto>>(apiService, bulkInsertService, syncModuleRepository) {

    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.PRODUCTS
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_LIST_ENDPOINT
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
            args = listOf(count, Res.string.job_product_parameters)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_product_parameters)
        )
    }

    override fun extractLastId(dtoData: List<ProductDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<ProductDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 500
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}