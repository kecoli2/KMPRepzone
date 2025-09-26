package com.repzone.sync.job.impl

import com.repzone.domain.model.SyncProductModel
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class ProductSyncJob(private val apiService: ISyncApiService<SyncProductModel>,
                     private val bulkInsertService: IBulkInsertService<SyncProductModel>
): RoleBasedSyncJob() {
    //region Field
    //endregion

    //region Properties
    override val allowedRoles = MERGE_ROLES
    override val jobType = SyncJobType.PRODUCTS
    //endregion

    //region Constructor
    //endregion

    //region Public Method


    override suspend fun executeSync(): Int {
        updateProgress(0, 100, "Fetching products...")
        checkCancellation()
        val result = apiService.fetchAll()
        if(result.isFailure){
            throw Exception("API Error: ${result.exceptionOrNull()?.message}")
        }
        val products = result.getOrThrow()
        updateProgress(50, 100, "${products.size} ürün alındı, veritabanına yazılıyor...")
        checkCancellation()

        val insertedCount = bulkInsertService.clearAndInsert(products)
        updateProgress(100, 100, "$insertedCount ürün kaydedildi")
        return insertedCount
    }


    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}