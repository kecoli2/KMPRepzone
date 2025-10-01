package com.repzone.sync.interfaces

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.model.SyncPage

interface ISyncApiService<T> {
    suspend fun fetchAll(model: SyncModuleModel): ApiResult<List<T>>
    suspend fun fetchPage(model: SyncModuleModel, page: Int, size: Int): ApiResult<SyncPage<T>>
}