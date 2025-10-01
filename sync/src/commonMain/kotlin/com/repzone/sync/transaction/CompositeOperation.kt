package com.repzone.sync.transaction

data class CompositeOperation(
    var id: Long = 0,
    val operations: List<TableOperation>,
    val description: String = "Composite Operation"
)