package com.repzone.data.repository.imp

import com.repzone.data.mapper.SyncModuleEntityDbMapper
import com.repzone.database.SyncModuleEntityQueries
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.ISyncModuleRepository

class SyncModuleRepositoryImpl(
    private val mapper: SyncModuleEntityDbMapper,
    private val queries: SyncModuleEntityQueries
): ISyncModuleRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun deleteById(id: Long) {
        queries.deleteSyncModuleEntity(id)
    }

    override suspend fun getAll(): List<SyncModuleModel> {
        return queries.selectAllSyncModuleEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long): SyncModuleModel? {
        return queries.selectBySyncModuleEntitySyncType(id).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncModuleModel) {
        queries.insertOrReplaceSyncModuleEntity(mapper.fromDomain(entity))
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}