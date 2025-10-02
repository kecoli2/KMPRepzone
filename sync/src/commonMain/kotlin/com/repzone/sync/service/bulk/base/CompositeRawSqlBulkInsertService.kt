package com.repzone.sync.service.bulk.base

import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.OperationResult
import com.repzone.sync.transaction.TransactionCoordinator

abstract class CompositeRawSqlBulkInsertService<T>(
    private val coordinator: TransactionCoordinator
) : IBulkInsertService<T> {

    override suspend fun insertBatch(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0
        val compositeOp = buildCompositeOperation(items, includeClears = false)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> {
                result.recordCount
            }
            is OperationResult.Error -> {
                throw RuntimeException("Composite insert failed: ${result.error}")
            }
        }
    }

    override suspend fun clearAndInsert(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0

        val compositeOp = buildCompositeOperation(items, includeClears = true)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> {
                result.recordCount
            }
            is OperationResult.Error -> {
                throw RuntimeException("Composite clear and insert failed: ${result.error}")
            }
        }
    }

    override suspend fun upsertBatch(items: T): Int {
        if(items as List<Any?> == emptyList<Any?>())
            return 0

        val compositeOp = buildCompositeOperation(items, includeClears = false, useUpsert = true)

        return when (val result = coordinator.executeCompositeOperation(compositeOp)) {
            is OperationResult.Success -> {
                result.recordCount
            }
            is OperationResult.Error -> {
                throw RuntimeException("Composite upsert failed: ${result.error}")
            }
        }
    }
    protected abstract fun buildCompositeOperation(items: T, includeClears: Boolean = false, useUpsert: Boolean = false): CompositeOperation
}