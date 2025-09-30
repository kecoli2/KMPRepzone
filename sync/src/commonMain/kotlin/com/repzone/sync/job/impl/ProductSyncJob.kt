package com.repzone.sync.job.impl

import com.repzone.domain.model.SyncProductModel
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.RoleBasedSyncJob
import com.repzone.sync.model.SyncJobType


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
        val response = apiService.fetchAll()
        var products: List<SyncProductModel>? = null

        when(response){
            is ApiResult.Success ->{
                products = response.data
            }

            is ApiResult.Error -> {
                throw Exception("API Error: ${response.exception.message}")
            }

            is ApiResult.Loading -> {
                updateProgress(25, 100, "Customer cekiliyor")
            }
        }
        updateProgress(50, 100, "${products?.size} ürün alındı, veritabanına yazılıyor...")
        checkCancellation()
        var insertedCount = 0
        products?.let {
            insertedCount = bulkInsertService.clearAndInsert(products)
        }

        updateProgress(100, 100, "$insertedCount ürün kaydedildi")
        return insertedCount
    }


    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}