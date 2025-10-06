package com.repzone.sync.transaction

data class TableOperation(
    val tableName: String,
    val clearSql: String? = null,
    val columns: List<String>,
    val values: List<String>,
    val recordCount: Int,
    val useUpsert: Boolean = false,
    val includeClears: Boolean = false,
    val includeOtherTableCount: Boolean = true
)