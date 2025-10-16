package com.repzone.sync.job.impl

import com.repzone.core.constant.IRouteApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.RouteDto
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
import repzonemobile.core.generated.resources.job_rota

class RouteDataSyncJob(apiService: ISyncApiService<List<RouteDto>>,
                       bulkInsertService: IBulkInsertService<List<RouteDto>>,
                       syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<RouteDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.ROUTE
    override val defaultRequestEndPoint = IRouteApiControllerConstant.ROUTE_LIST_ENDPOINT
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
            args = listOf(count, Res.string.job_rota)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_rota)
        )
    }

    override fun extractLastId(dtoData: List<RouteDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0L
    }

    override fun getDataSize(dtoData: List<RouteDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 1000
        return value
    }
    //endregion

}