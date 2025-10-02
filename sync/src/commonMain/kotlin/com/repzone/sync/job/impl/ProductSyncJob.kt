package com.repzone.sync.job.impl

import com.repzone.core.constant.IProductApiControllerConstant
import com.repzone.core.util.toModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType


class ProductSyncJob(private val apiService: ISyncApiService<List<MobileProductDto>>,
                     private val bulkInsertService: IBulkInsertService<List<MobileProductDto>>,
                     syncModuleRepository: ISyncModuleRepository
): RoleBasedSyncJob(syncModuleRepository) {
    //region Field
    //endregion

    //region Properties
    override val allowedRoles = MERGE_ROLES
    override val jobType = SyncJobType.PRODUCTS
    override val defaultRequestEndPoint = IProductApiControllerConstant.PRODUCT_LIST_ENDPOINT
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 100
        return value
    }

    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching products...")
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
                        val products = result.data
                        totalFetched += products.size

                        updateProgress(25, 100, "Fetched $totalFetched products...")
                        checkCancellation()
                        val inserted = bulkInsertService.upsertBatch(result.data)
                        getSyncModuleModel()?.requestFilter?.toModel<FilterModelRequest>()?.lastId = products.lastOrNull()?.id ?: 0
                        totalInserted += inserted
                    }
                }
            }
        updateProgress(100, 100, "$totalFetched product saved...")
        return totalInserted
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}