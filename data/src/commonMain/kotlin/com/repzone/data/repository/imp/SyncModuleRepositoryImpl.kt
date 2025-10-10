package com.repzone.data.repository.imp

import com.repzone.core.enums.UIModule
import com.repzone.core.util.extensions.enumToLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncModuleEntity
import com.repzone.database.SyncModuleEntityQueries
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.ISyncModuleRepository

class SyncModuleRepositoryImpl(
    private val mapper: Mapper<SyncModuleEntity, SyncModuleModel>,
    private val queries: SyncModuleEntityQueries
): ISyncModuleRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun deleteById(id: Long, module: UIModule) {
        queries.deleteSyncModuleEntity(id, module.enumToLong())
    }

    override suspend fun getAll(): List<SyncModuleModel> {
        return queries.selectAllSyncModuleEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long, module: UIModule): SyncModuleModel? {
        return queries.selectBySyncModuleEntitySyncType(id, module.enumToLong()).executeAsOneOrNull()?.let { mapper.toDomain(it) }
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