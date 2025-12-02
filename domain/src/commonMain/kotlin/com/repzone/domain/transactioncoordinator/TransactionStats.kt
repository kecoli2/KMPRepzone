package com.repzone.domain.transactioncoordinator

data class TransactionStats(
    val totalOperations: Long,
    val successfulOperations: Long,
    val failedOperations: Long,
    val queueSize: Int
)