package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ProductDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class ProductSyncJob(apiService: ISyncApiService<List<ProductDto>>, bulkInsertService: IBulkInsertService<List<ProductDto>>, syncModuleRepository: ISyncModuleRepository)
    : BasePaginatedSyncJob<List<ProductDto>>(apiService, bulkInsertService, syncModuleRepository) {

    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.PRODUCTS
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_LIST_ENDPOINT
    override val fetchingMessage = "Fetching products..."
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int) = "Fetched $count products..."
    override fun getCompletedMessage(count: Int) = "$count products saved..."

    override fun extractLastId(dtoData: List<ProductDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<ProductDto>): Int {
        return dtoData.size
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}