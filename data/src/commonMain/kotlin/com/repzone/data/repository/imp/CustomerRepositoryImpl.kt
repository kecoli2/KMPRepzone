package com.repzone.data.repository.imp

import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository

class CustomerRepositoryImpl(private val mapper: CustomerEntityDbMapper,
                             private val iDatabaseManager: IDatabaseManager): ICustomerRepository {
    //region Public Method
    override suspend fun deleteById(id: Long) {
        iDatabaseManager.getDatabase().syncCustomerEntityQueries.deleteSyncCustomerEntity(id)
    }

    override suspend fun getAll(): List<SyncCustomerModel> {
        return iDatabaseManager.getDatabase().syncCustomerEntityQueries.selectAllSyncCustomerEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long): SyncCustomerModel? {
        return iDatabaseManager.getDatabase().syncCustomerEntityQueries.selectBySyncCustomerEntityId(id).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncCustomerModel) {
        iDatabaseManager.getDatabase().syncCustomerEntityQueries.insertOrReplaceSyncCustomerEntity(mapper.fromDomain (entity))
    }

    override suspend fun pending(): List<SyncCustomerModel> {
        return iDatabaseManager.getDatabase().syncCustomerEntityQueries.selectAllSyncCustomerEntity() .executeAsList().map { mapper.toDomain(it) }
    }
    //endregion

}