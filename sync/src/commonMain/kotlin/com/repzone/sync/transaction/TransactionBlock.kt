package com.repzone.sync.transaction

data class TransactionBlock<T>(
    var id: Long = 0,
    val description: String,
    val block: suspend () -> T
)