package com.repzone.sync.service.bulk

import com.repzone.data.mapper.SyncCustomerEntityDbMapper
import com.repzone.database.AppDatabase
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.sync.service.bulk.base.BaseBulkInsertService
import kotlin.collections.chunked
import kotlin.collections.forEach

class CustomerBulkInsertService(private val database: AppDatabase,
                                private val dbMapper: SyncCustomerEntityDbMapper)
    : BaseBulkInsertService<SyncCustomerModel, SyncCustomerEntity>(database){
    //region Field
    private val queries = database.syncCustomerEntityQueries

    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun performBulkInsert(items: List<SyncCustomerModel>) {
        items.chunked(500).forEach { chunk ->
            chunk.forEach { item ->
                queries.insertSyncCustomerEntity(dbMapper.fromDomain(item))
            }
        }
    }

    /**
     * Tüm tablo burada istenir ise boşaltılır
     */
    override fun performBulkClear() {
        //queries.clearAll()
    }

    override fun performBulkUpsert(items: List<SyncCustomerModel>) {
        items.chunked(500).forEach { chunk ->
            chunk.forEach { item ->
                queries.insertOrReplaceSyncCustomerEntity(dbMapper.fromDomain(item))
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}