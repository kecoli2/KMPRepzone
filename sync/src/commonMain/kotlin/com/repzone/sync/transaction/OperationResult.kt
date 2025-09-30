package com.repzone.sync.transaction

sealed class OperationResult {
    data class Success(
        val recordCount: Int,
        val duration: Long,
        val operationId: Long
    ) : OperationResult()

    data class Error(
        val error: String,
        val duration: Long,
        val operationId: Long
    ) : OperationResult()
}