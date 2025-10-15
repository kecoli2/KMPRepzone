package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ProductGroupDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_product_group

class ProductGroupSyncJob(apiService: ISyncApiService<List<ProductGroupDto>>,
                          bulkInsertService: IBulkInsertService<List<ProductGroupDto>>,
                          syncModuleRepository: ISyncModuleRepository
): BasePaginatedSyncJob<List<ProductGroupDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.PRODUCTS_GROUP
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_GROUP_LIST_ENDPOINT
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
            args = listOf(count, Res.string.job_product_group)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_product_group)
        )
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