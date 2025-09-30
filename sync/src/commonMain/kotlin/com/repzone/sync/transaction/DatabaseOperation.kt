package com.repzone.sync.transaction

data class DatabaseOperation(
    val type: OperationType,
    val tableName: String,
    val columns: List<String>,
    val values: List<String>, // Pre-formatted VALUES clauses
    val clearSql: String = "",
    val recordCount: Int = 0,
    var id: Long = 0
)