package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.util.toModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole
import com.repzone.sync.util.SyncConstant

class ProductGroupSyncJob(private val apiService: ISyncApiService<List<ServiceProductGroupDto>>,
                          private val bulkInsertService: IBulkInsertService<List<ServiceProductGroupDto>>,
                          syncModuleRepository: ISyncModuleRepository
) : RoleBasedSyncJob(syncModuleRepository){
    //region Field
    override val allowedRoles: Set<UserRole> = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_GROUP_LIST_ENDPOINT
    override val jobType = SyncJobType.PRODUCTS_GROUP
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching product groups...")
        checkCancellation()
        var totalInserted = 0
        var totalFetched = 0
        val requestFilter = getSyncModuleModel()?.requestFilter?.toModel<FilterModelRequest>()
        apiService.fetchPage(getSyncModuleModel()!!, requestFilter!!.take)
            .collect { result ->
                when(result){
                    is ApiResult.Error -> {
                        throw Exception("API Error: ${result.exception.message}")
                    }
                    is ApiResult.Success -> {
                        val groups = result.data
                        totalFetched += groups.size
                        updateProgress(25, 100, "Fetched $totalFetched products...")
                        val inserted = bulkInsertService.upsertBatch(groups)
                        getSyncModuleModel()?.requestFilter?.toModel<FilterModelRequest>()?.lastId = groups.lastOrNull()?.id ?: 0
                        totalInserted += inserted
                    }
                    else -> {

                    }
                }
            }
        updateProgress(100, 100, "$totalFetched product group saved...")
        return totalInserted
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = SyncConstant.TAKEN_COUNT
        return value
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}