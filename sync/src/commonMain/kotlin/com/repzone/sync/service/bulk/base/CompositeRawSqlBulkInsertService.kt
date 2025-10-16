package com.repzone.sync.service.bulk.base

import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.OperationResult
import com.repzone.sync.transaction.TransactionCoordinator

abstract class CompositeRawSqlBulkInsertService<T>(private val coordinator: TransactionCoordinator) : IBulkInsertService<T> {

    //region Public Method
    override suspend fun insertBatch(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0
        val compositeOp = buildCompositeOperationWithChunking(items, includeClears = false)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> result.recordCount
            is OperationResult.Error -> throw RuntimeException("Composite insert failed: ${result.error}")
        }
    }

    override suspend fun clearAndInsert(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0

        val compositeOp = buildCompositeOperationWithChunking(items, includeClears = true)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> result.recordCount
            is OperationResult.Error -> throw RuntimeException("Composite clear and insert failed: ${result.error}")
        }
    }

    override suspend fun upsertBatch(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0

        val compositeOp = buildCompositeOperationWithChunking(items, includeClears = false, useUpsert = true)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> result.recordCount
            is OperationResult.Error -> throw RuntimeException("Composite upsert failed: ${result.error}")
        }
    }

    //endregion

    //region Protected Method
    protected abstract fun buildCompositeOperation(items: T, includeClears: Boolean = false, useUpsert: Boolean = false): CompositeOperation
    //endregion

    //region Private Method
    private fun buildCompositeOperationWithChunking(items: T, includeClears: Boolean = false, useUpsert: Boolean = false): CompositeOperation {
        val rawOp = buildCompositeOperation(items, includeClears, useUpsert)
        val chunkedOperations = rawOp.operations.map { tableOp ->
            if (tableOp.recordCount == 0) return@map tableOp
            val chunkSize = calculateChunkSize(tableOp.columns.size)
            val chunkedValues = tableOp.values.chunked(chunkSize).flatMap { it }
            tableOp.copy(values = chunkedValues)
        }
        return rawOp.copy(operations = chunkedOperations)
    }

    private fun calculateChunkSize(columnCount: Int): Int {
        return when (columnCount) {
            in 1..10 -> 3000
            in 11..30 -> 2500
            in 31..50 -> 1500
            else -> 1000
        }
    }
    //endregion







}