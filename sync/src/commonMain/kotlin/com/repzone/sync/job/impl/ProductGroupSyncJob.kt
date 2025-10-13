package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ProductGroupDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class ProductGroupSyncJob(apiService: ISyncApiService<List<ProductGroupDto>>,
                          bulkInsertService: IBulkInsertService<List<ProductGroupDto>>,
                          syncModuleRepository: ISyncModuleRepository
): BasePaginatedSyncJob<List<ProductGroupDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.PRODUCTS_GROUP
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_GROUP_LIST_ENDPOINT
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count product group..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count product group saved..."
    }

    override fun extractLastId(dtoData: List<ProductGroupDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<ProductGroupDto>): Int {
        return dtoData.size
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}