package com.repzone.sync.job.impl

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType

class CustomerSyncJob(private val apiService: ISyncApiService<SyncCustomerModel>,
                      private val bulkInsertService: IBulkInsertService<SyncCustomerModel>):RoleBasedSyncJob() {
    //region Field
    //endregion

    //region Properties
    override val allowedRoles = MERGE_ROLES
    override val jobType = SyncJobType.CUSTOMERS
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching customers...")
        checkCancellation()
        val result = apiService.fetchAll()
        if(result.isFailure){
            throw Exception("API Error: ${result.exceptionOrNull()?.message}")
        }
        val customer = result.getOrThrow()
        updateProgress(50, 100, "${customer.size} customer alındı, veritabanına yazılıyor...")
        checkCancellation()

        val insertedCount = bulkInsertService.clearAndInsert(customer)
        updateProgress(100, 100, "$insertedCount Musteriler kaydedildi")
        return insertedCount
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}