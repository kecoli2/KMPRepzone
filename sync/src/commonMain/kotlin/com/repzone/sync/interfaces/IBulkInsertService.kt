package com.repzone.sync.interfaces

interface IBulkInsertService<T> {
    suspend fun insertBatch(items: T): Int
    suspend fun clearAndInsert(items: T): Int
    suspend fun upsertBatch(items: T): Int
}