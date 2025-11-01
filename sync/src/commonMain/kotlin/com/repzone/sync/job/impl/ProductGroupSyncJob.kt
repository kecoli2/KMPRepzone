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
import com.repzone.core.util.extensions.toDateString
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.models.request.FilterModelRequest
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_product_group
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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

    override fun extractLastIdAndLastDate(dtoData: List<ProductGroupDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<ProductGroupDto>): Int {
        return dtoData.size
    }
    //endregion

}