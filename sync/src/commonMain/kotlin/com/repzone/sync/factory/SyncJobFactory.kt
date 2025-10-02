package com.repzone.sync.factory

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.job.impl.CustomerSyncJob
import com.repzone.sync.job.impl.ProductGroupSyncJob
import com.repzone.sync.job.impl.ProductSyncJob
import com.repzone.sync.model.SyncJobType

class SyncJobFactory(private val syncModuleRepository: ISyncModuleRepository) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    fun createJobs(
        productApi: ISyncApiService<List<MobileProductDto>>,
        productBulkInsert: IBulkInsertService<List<MobileProductDto>>,
        customerApi: ISyncApiService<List<SyncCustomerModel>>,
        customerBulkInsert: IBulkInsertService<List<SyncCustomerModel>>,
        productGroupApi: ISyncApiService<List<ServiceProductGroupDto>>,
        productGroupBulkInsert: IBulkInsertService<List<ServiceProductGroupDto>>
    ): Map<SyncJobType, ISyncJob> {

        return mapOf(
            SyncJobType.PRODUCTS to ProductSyncJob(productApi, productBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS to CustomerSyncJob(customerApi, customerBulkInsert, syncModuleRepository),
            SyncJobType.PRODUCTS_GROUP to ProductGroupSyncJob(productGroupApi, productGroupBulkInsert, syncModuleRepository),
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}