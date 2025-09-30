package com.repzone.data.repository.imp

import com.repzone.data.mapper.SyncCustomerEntityDbMapper
import com.repzone.data.repository.base.BaseCrudRepository
import com.repzone.data.util.IDbCrudOps
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncCustomerEntityQueries
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository

class CustomerRepositoryImpl(ops: IDbCrudOps<Long, SyncCustomerEntity>, private val mapper: SyncCustomerEntityDbMapper,
                             private val queries: SyncCustomerEntityQueries):BaseCrudRepository<Long, SyncCustomerEntity, SyncCustomerModel>(ops, mapper),
    ICustomerRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun deleteById(id: Long) {
        super.deleteById(id)
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