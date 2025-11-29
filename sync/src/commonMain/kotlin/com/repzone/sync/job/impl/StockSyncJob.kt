package com.repzone.sync.job.impl

import com.repzone.core.constant.IStockApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.SyncStockDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobType
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_product_stock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class StockSyncJob(apiService: ISyncApiService<List<SyncStockDto>>,
                   bulkinsertService: IBulkInsertService<List<SyncStockDto>>,
                   syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<SyncStockDto>>(apiService, bulkinsertService, syncModuleRepository) {

    //region ------------- Field -------------
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.STOCK
    override val defaultRequestEndPoint = IStockApiControllerConstant.STOCK_MAIN_LIST_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.PRODUCT
    //endregion ------------- Field -------------

    //region ------------- Constructor -------------
    //endregion ------------- Constructor -------------

    //region ------------- Public Method -------------
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_product_stock)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_product_stock)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<SyncStockDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
            requestModel?.lastModDate = it.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""

        }
    }

    override fun getDataSize(dtoData: List<SyncStockDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 500
        value.lastId = 0
        return value
    }
    //endregion ------------- Public Method -------------

    //region ------------- Private Method -------------
    //endregion ------------- Private Method -------------
}