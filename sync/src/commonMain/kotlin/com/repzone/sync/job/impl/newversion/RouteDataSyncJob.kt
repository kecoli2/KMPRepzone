package com.repzone.sync.job.impl.newversion

import com.repzone.core.constant.IRouteApiControllerConstant
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.RouteDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class RouteDataSyncJob(apiService: ISyncApiService<List<RouteDto>>,
                       bulkInsertService: IBulkInsertService<List<RouteDto>>,
                       syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<RouteDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.ROUTE
    override val defaultRequestEndPoint = IRouteApiControllerConstant.ROUTE_LIST_ENDPOINT
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count route..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count route saved..."
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

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}