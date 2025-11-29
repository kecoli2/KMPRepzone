package com.repzone.sync.job.impl


import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.SyncUnitDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobType
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ProductUnitJob(apiService: ISyncApiService<List<SyncUnitDto>>,
                     bulkinsertService: IBulkInsertService<List<SyncUnitDto>>,
                     syncModuleRepository: ISyncModuleRepository): BasePaginatedSyncJob<List<SyncUnitDto>>(apiService, bulkinsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.PRODUCT_UNIT
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_UNIT_LIST_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.PRODUCT
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_product_unit)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_product_unit)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<SyncUnitDto>, requestModel: FilterModelRequest?) {
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<SyncUnitDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        return value
    }
    //endregion
}