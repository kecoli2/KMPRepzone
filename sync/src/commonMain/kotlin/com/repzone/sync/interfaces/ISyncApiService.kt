package com.repzone.sync.interfaces

import com.repzone.sync.model.SyncPage

interface ISyncApiService<T> {
    suspend fun fetchAll(): Result<List<T>>
    suspend fun fetchUpdatedSince(sinceIso: String): Result<List<T>>
    suspend fun fetchPage(page: Int, size: Int): Result<SyncPage<T>>
}