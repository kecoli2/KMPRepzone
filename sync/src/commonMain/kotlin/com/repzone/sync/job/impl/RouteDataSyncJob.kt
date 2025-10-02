package com.repzone.sync.job.impl

import com.repzone.core.constant.IRouteApiControllerConstant
import com.repzone.core.util.toModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.MobileRouteDto
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole
import com.repzone.sync.util.SyncConstant

class RouteDataSyncJob(private val apiService: ISyncApiService<List<MobileRouteDto>>,
                       private val bulkInsertService: IBulkInsertService<List<MobileRouteDto>>,
                       syncModuleRepository: ISyncModuleRepository,
                       ): RoleBasedSyncJob(syncModuleRepository) {
    //region Field
    override val allowedRoles: Set<UserRole> = setOf(UserRole.ADMIN, UserRole.SALES_REP)
    override val defaultRequestEndPoint = IRouteApiControllerConstant.ROUTE_LIST_ENDPOINT
    override val jobType = SyncJobType.ROUTE
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching route...")
        checkCancellation()
        var totalInserted = 0
        var totalFetched = 0
        val requestFilter = getSyncModuleModel()?.requestFilter?.toModel<FilterModelRequest>()

        apiService.fetchPage(getSyncModuleModel()!!, requestFilter?.take ?: 100 )
            .collect { result ->
                when(result){
                    is ApiResult.Error -> {
                        throw Exception("API Error: ${result.exception.message}")
                    }
                    is ApiResult.Loading -> {

                    }
                    is ApiResult.Success -> {
                        val route = result.data
                        totalFetched += route.size

                        updateProgress(25, 100, "Fetched $totalFetched route...")
                        checkCancellation()
                        val inserted = bulkInsertService.upsertBatch(result.data)
                        getSyncModuleModel()?.requestFilter?.toModel<FilterModelRequest>()?.lastId = route.lastOrNull()?.id ?: 0
                        totalInserted += inserted
                    }
                }
            }
        updateProgress(100, 100, "$totalFetched route saved...")
        return totalInserted
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