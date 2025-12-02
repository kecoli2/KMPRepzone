package com.repzone.domain.transactioncoordinator

data class TransactionBlock<T>(
    var id: Long = 0,
    val description: String,
    val block: suspend () -> T
)