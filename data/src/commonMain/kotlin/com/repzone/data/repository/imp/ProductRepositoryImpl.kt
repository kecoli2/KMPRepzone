package com.repzone.data.repository.imp

import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.database.SyncProductEntityQueries
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.repository.IProductRepository

class ProductRepositoryImpl(private val mapper: ProductEntityDbMapper,
                            private val queries: SyncProductEntityQueries
): IProductRepository {
    //region Public Method
    override suspend fun deleteById(id: Long) {
        queries.deleteSyncProductEntity(id)
    }

    override suspend fun getAll(): List<SyncProductModel> {
        return queries.selectAllSyncProductEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long): SyncProductModel? {
        return queries.selectBySyncProductEntityId(id).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncProductModel) {
        queries.insertOrReplaceSyncProductEntity(mapper.fromDomain(entity))
    }

    //endregion

}