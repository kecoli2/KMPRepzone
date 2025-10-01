package com.repzone.data.repository.imp

import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.SyncCustomerEntityQueries
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository

class CustomerRepositoryImpl(private val mapper: CustomerEntityDbMapper, private val queries: SyncCustomerEntityQueries): ICustomerRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun deleteById(id: Long) {
        queries.deleteSyncCustomerEntity(id)
    }

    override suspend fun getAll(): List<SyncCustomerModel> {
        return queries.selectAllSyncCustomerEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long): SyncCustomerModel? {
        return queries.selectBySyncCustomerEntityId(id).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncCustomerModel) {
        queries.insertOrReplaceSyncCustomerEntity(mapper.fromDomain (entity))
    }

    override suspend fun pending(): List<SyncCustomerModel> {
        return queries.selectAllSyncCustomerEntity() .executeAsList().map { mapper.toDomain(it) }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}