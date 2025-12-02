package com.repzone.sync.transaction

sealed class TransactionResult<T> {
    data class Success<T>(
        val data: T,
        val duration: Long,
        val operationId: Long
    ) : TransactionResult<T>()

    data class Error<T>(
        val error: String,
        val duration: Long,
        val operationId: Long
    ) : TransactionResult<T>()
}