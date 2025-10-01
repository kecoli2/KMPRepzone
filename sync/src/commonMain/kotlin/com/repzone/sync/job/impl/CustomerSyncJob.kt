package com.repzone.sync.job.impl

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType

class CustomerSyncJob(private val apiService: ISyncApiService<SyncCustomerModel>,
                      private val bulkInsertService: IBulkInsertService<SyncCustomerModel>,
                      syncModuleRepository: ISyncModuleRepository
): RoleBasedSyncJob(syncModuleRepository) {
    //region Field
    //endregion

    //region Properties
    override val allowedRoles = MERGE_ROLES
    override val jobType = SyncJobType.CUSTOMERS
    override val defaultRequestEndPoint = ""
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching customers...")
        checkCancellation()
        val response = apiService.fetchAll(getSyncModuleModel()!!)
        var customer : List<SyncCustomerModel>? = null
        when(response){
            is ApiResult.Success ->{
                customer = response.data
            }

            is ApiResult.Error -> {
                throw Exception("API Error: ${response.exception.message}")
            }

            ApiResult.Loading -> {
                updateProgress(25, 100, "Customer cekiliyor")
            }
        }

        updateProgress(50, 100, "${customer?.size} customer alındı, veritabanına yazılıyor...")
        checkCancellation()
        var  insertedCount = 0
        customer?.let {
            insertedCount = bulkInsertService.clearAndInsert(customer)
            updateProgress(100, 100, "$insertedCount Musteriler kaydedildi")
        }

        return insertedCount
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}