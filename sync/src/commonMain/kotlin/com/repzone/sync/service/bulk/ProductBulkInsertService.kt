package com.repzone.sync.service.bulk

import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.database.AppDatabase
import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.SyncProductModel
import com.repzone.sync.service.bulk.base.BaseBulkInsertService

class ProductBulkInsertService(private val database: AppDatabase,
                                private val dbMapper: ProductEntityDbMapper): BaseBulkInsertService<SyncProductModel, SyncProductEntity>(database) {
    //region Field
    private val queries = database.syncProductEntityQueries
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun performBulkInsert(items: List<SyncProductModel>) { // ← suspend kaldırıldı

        // Batch insert using chunked approach
        items.chunked(500).forEach { chunk ->
            chunk.forEach { item ->
                queries.insertSyncProductEntity(dbMapper.fromDomain(item))
            }
        }
    }

    /**
     * Tüm tablo burada istenir ise boşaltılır
     */
    override fun performBulkClear() {
        //queries.clearAll()
    }

    override fun performBulkUpsert(items: List<SyncProductModel>) { // ← suspend kaldırıldı
        items.chunked(500).forEach { chunk ->
            chunk.forEach { item ->
                queries.insertOrReplaceSyncProductEntity(dbMapper.fromDomain(item))
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}