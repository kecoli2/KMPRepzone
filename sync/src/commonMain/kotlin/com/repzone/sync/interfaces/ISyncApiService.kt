package com.repzone.sync.interfaces

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.http.wrapper.ApiResult
import kotlinx.coroutines.flow.Flow

interface ISyncApiService<T> {
    suspend fun fetchAll(model: SyncModuleModel): ApiResult<T>
    suspend fun fetchPage(model: SyncModuleModel, pageSize: Int): Flow<ApiResult<T>>
}