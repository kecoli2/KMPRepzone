package com.repzone.sync.service.bulk.base

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase
import com.repzone.sync.interfaces.IBulkInsertService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseBulkInsertService<T,DB>(private val appDatabase: AppDatabase):
    IBulkInsertService<T> {
    //region Field

    // Transaction mutex - sadece bir bulk operation aynı anda
    companion object {
        private val transactionMutex = Mutex()
    }

    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun insertBatch(items: List<T>): Int = transactionMutex.withLock {
        appDatabase.transactionWithResult {
            performBulkInsert(items)
            items.size
        }
    }

    override suspend fun clearAndInsert(items: List<T>): Int = transactionMutex.withLock {
        appDatabase.transactionWithResult {
            performBulkClear()
            performBulkInsert(items)
            items.size
        }
    }

    override suspend fun upsertBatch(items: List<T>): Int = transactionMutex.withLock {
        appDatabase.transactionWithResult {
            performBulkUpsert(items)
            items.size
        }
    }
    //endregion

    //region Protected Method
    protected abstract fun performBulkInsert(items: List<T>)
    protected abstract fun performBulkClear()
    protected abstract fun performBulkUpsert(items: List<T>)
    //endregion

    //region Private Method
    //endregion
}