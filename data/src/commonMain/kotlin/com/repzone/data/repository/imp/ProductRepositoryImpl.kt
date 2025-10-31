package com.repzone.data.repository.imp

import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.repository.IProductRepository

class ProductRepositoryImpl(private val mapper: ProductEntityDbMapper,
                            private val iDatabaseManager: IDatabaseManager
): IProductRepository {
    //region Public Method
    override suspend fun deleteById(id: Long) {
        iDatabaseManager.getDatabase().syncProductEntityQueries.deleteSyncProductEntity(id)
    }

    override suspend fun getAll(): List<SyncProductModel> {
        return iDatabaseManager.getDatabase().syncProductEntityQueries.selectAllSyncProductEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long): SyncProductModel? {
        return iDatabaseManager.getDatabase().syncProductEntityQueries.selectBySyncProductEntityId(id).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncProductModel) {
        iDatabaseManager.getDatabase().syncProductEntityQueries.insertOrReplaceSyncProductEntity(mapper.fromDomain(entity))
    }

    //endregion

}