package com.repzone.domain.transactioncoordinator

interface ITransactionCoordinator {
    suspend fun <T> executeTransaction(description: String = "Custom Transaction", block: suspend () -> T): TransactionResult<T>
    suspend fun executeCompositeOperation(compositeOp: CompositeOperation): OperationResult
    suspend fun executeBulkOperation(operation: DatabaseOperation): OperationResult
    suspend fun getStats(): TransactionStats
    fun shutdown()
}