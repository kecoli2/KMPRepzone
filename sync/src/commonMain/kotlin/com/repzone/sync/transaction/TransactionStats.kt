package com.repzone.sync.transaction

data class TransactionStats(
    val totalOperations: Long,
    val successfulOperations: Long,
    val failedOperations: Long,
    val queueSize: Int
)