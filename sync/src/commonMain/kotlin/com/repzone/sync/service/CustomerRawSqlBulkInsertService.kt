package com.repzone.sync.service

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerRawSqlBulkInsertService(
    private val dbMapper: Mapper<SyncCustomerEntity, SyncCustomerModel>,
    coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncCustomerModel>>(coordinator) {
    //region Field

    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun clearAndInsert(items: List<SyncCustomerModel>): Int {
        return 0
    }

    override suspend fun insertBatch(items: List<SyncCustomerModel>): Int {
        return 0
    }

    override suspend fun upsertBatch(items: List<SyncCustomerModel>): Int {
        return 0
    }

    override fun buildCompositeOperation(items: List<SyncCustomerModel>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        TODO("Not yet implemented")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}