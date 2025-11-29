package com.repzone.database.runtime

import com.repzone.core.platform.Logger

/**
 * SQL Query Logger - Parametreleri SQL içine substitute eder
 */
object SqlQueryLogger {

    //region ------------- Field -------------
    private const val TAG = "SQL_QUERY"
    //endregion ------------- Field -------------

    //region ------------- Constructor -------------
    //endregion ------------- Constructor -------------

    //region ------------- Public Method -------------
    /**
     * Raw query logging - Parametreleri doğrudan SQL içine yerleştirir
     */
    fun logRawQuery(sql: String, parameters: List<Any?>) {
        log("═".repeat(43))

        // Parametreleri SQL içine substitute et
        var executableSql = sql
        parameters.forEach { param ->
            val formattedValue = when (param) {
                null -> "NULL"
                is String -> "'$param'"
                else -> param.toString()
            }
            executableSql = executableSql.replaceFirst("?", formattedValue)
        }

        log(executableSql)
        log("═".repeat(43))
    }

    /**
     * INSERT logging - with columns and values
     */
    fun logInsert(tableName: String, columns: List<String>, values: List<Any?>) {
        log("═".repeat(43))

        val columnsPart = columns.joinToString(", ")
        val valuesPart = values.joinToString(", ") { formatValue(it) }

        log("INSERT INTO $tableName ($columnsPart) VALUES ($valuesPart)")
        log("═".repeat(43))
    }

    /**
     * UPDATE logging - with set columns, where clause, and parameters
     */
    fun logUpdate(tableName: String, setColumns: List<String>, whereClause: String, parameters: List<Any?>) {
        log("═".repeat(43))

        // Build SET clause
        val setClauses = setColumns.zip(parameters.dropLast(1)).joinToString(", ") { (col, value) ->
            "$col = ${formatValue(value)}"
        }

        // Build WHERE clause (last parameter)
        val whereValue = formatValue(parameters.last())
        val finalWhereClause = whereClause.replace("?", whereValue)

        log("UPDATE $tableName SET $setClauses WHERE $finalWhereClause")
        log("═".repeat(43))
    }

    /**
     * DELETE logging - with where clause and parameters
     */
    fun logDelete(tableName: String, whereClause: String, parameters: List<Any?>) {
        log("═".repeat(43))

        // Substitute parameters into WHERE clause
        var finalWhereClause = whereClause
        parameters.forEach { param ->
            finalWhereClause = finalWhereClause.replaceFirst("?", formatValue(param))
        }

        log("DELETE FROM $tableName WHERE $finalWhereClause")
        log("═".repeat(43))
    }

    /**
     * Transaction logging
     */
    fun logTransactionStart() {
        log("▶ TRANSACTION BEGIN")
    }

    fun logTransactionCommit() {
        log("✓ TRANSACTION COMMIT")
    }

    fun logTransactionRollback(error: Throwable) {
        log("✗ TRANSACTION ROLLBACK: ${error.message}")
    }

    /**
     * Query execution time
     */
    fun logQueryTime(operation: String, timeSeconds: Long) {
        log("⏱ $operation took ${timeSeconds}s")
    }

    /**
     * Result count
     */
    fun logResultCount(count: Int, timeMs: Long) {
        Logger.d("SQL_RESULT", "Returned $count rows in ${timeMs}ms")
    }

    /**
     * Batch operations
     */
    fun logBatch(operation: String, tableName: String, count: Int) {
        log("═".repeat(43))
        log("BATCH $operation: $tableName")
        log("Count: $count items")
        log("═".repeat(43))
    }

    /**
     * SQL string içindeki ? placeholderlarını gerçek değerlerle değiştirir
     * SelectBuilder.toSqlStringWithParams() tarafından kullanılır
     */
    fun substituteSqlParams(sql: String, parameters: List<Any?>): String {
        var result = sql
        parameters.forEach { param ->
            result = result.replaceFirst("?", formatValue(param))
        }
        return result
    }
    //endregion ------------- Public Method -------------

    //region ------------- Private Method -------------
    private fun log(message: String) {
        Logger.d(TAG, message)
    }

    private fun formatValue(value: Any?): String = when (value) {
        null -> "NULL"
        is String -> "'$value'"
        else -> value.toString()
    }
    //endregion ------------- Private Method -------------
}