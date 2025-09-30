package com.repzone.sync.interfaces

interface IBulkInsertService<T> {
    suspend fun insertBatch(items: List<T>): Int
    suspend fun clearAndInsert(items: List<T>): Int
    suspend fun upsertBatch(items: List<T>): Int
}