package com.repzone.sync.interfaces

import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.model.SyncPage

interface ISyncApiService<T> {
    suspend fun fetchAll(): ApiResult<List<T>>
    suspend fun fetchUpdatedSince(sinceIso: String): ApiResult<List<T>>
    suspend fun fetchPage(page: Int, size: Int): ApiResult<SyncPage<T>>
}