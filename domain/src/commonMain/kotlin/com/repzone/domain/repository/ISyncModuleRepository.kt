package com.repzone.domain.repository

import com.repzone.core.enums.UIModule
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.repository.base.CrudRepository

interface ISyncModuleRepository {
    suspend fun deleteById(id: Long, module: UIModule)
    suspend fun getAll(): List<SyncModuleModel>
    suspend fun getById(id: Long, module: UIModule): SyncModuleModel?
    suspend fun upsert(entity: SyncModuleModel)
}