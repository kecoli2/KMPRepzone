package com.repzone.data.repository.imp

import com.repzone.core.enums.UIModule
import com.repzone.core.util.extensions.enumToLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncModuleEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.ISyncModuleRepository

class SyncModuleRepositoryImpl(
    private val mapper: Mapper<SyncModuleEntity, SyncModuleModel>,
    private val iDatabaseManager: IDatabaseManager): ISyncModuleRepository {
    //region Public Method
    override suspend fun deleteById(id: Long, module: UIModule) {
        iDatabaseManager.getDatabase().syncModuleEntityQueries.deleteSyncModuleEntity(id, module.enumToLong())
    }

    override suspend fun getAll(): List<SyncModuleModel> {
        return iDatabaseManager.getDatabase().syncModuleEntityQueries.selectAllSyncModuleEntity().executeAsList().map { mapper.toDomain(it) }
    }

    override suspend fun getById(id: Long, module: UIModule): SyncModuleModel? {
        return iDatabaseManager.getDatabase().syncModuleEntityQueries.selectBySyncModuleEntitySyncType(id, module.enumToLong()).executeAsOneOrNull()?.let { mapper.toDomain(it) }
    }

    override suspend fun upsert(entity: SyncModuleModel) {
        iDatabaseManager.getDatabase().syncModuleEntityQueries.insertOrReplaceSyncModuleEntity(mapper.fromDomain(entity))
    }
    //endregion

}