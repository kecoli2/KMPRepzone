package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class ProductGroupSyncJob(apiService: ISyncApiService<List<ServiceProductGroupDto>>,
                          bulkInsertService: IBulkInsertService<List<ServiceProductGroupDto>>,
                          syncModuleRepository: ISyncModuleRepository
): BasePaginatedSyncJob<List<ServiceProductGroupDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.PRODUCTS_GROUP
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_GROUP_LIST_ENDPOINT
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

    override fun extractLastId(dtoData: List<ServiceProductGroupDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<ServiceProductGroupDto>): Int {
        return dtoData.size
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}