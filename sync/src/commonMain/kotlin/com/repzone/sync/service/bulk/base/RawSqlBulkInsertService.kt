package com.repzone.sync.service.bulk.base

import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.transaction.DatabaseOperation
import com.repzone.sync.transaction.OperationResult
import com.repzone.sync.transaction.OperationType
import com.repzone.sync.transaction.TransactionCoordinator


abstract class RawSqlBulkInsertService<T>(private val coordinator: TransactionCoordinator): IBulkInsertService<T> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    /**
     * Bulk INSERT operation
     * Items'ları queue'ya gönderir, coordinator sırayla işler
     */
    override suspend fun insertBatch(items: List<T>): Int {
        if (items.isEmpty()) return 0

        val operation = buildDatabaseOperation(items, OperationType.INSERT)

        return when (val result = coordinator.executeBulkOperation(operation)) {
            is OperationResult.Success -> {
                logSuccess("INSERT", result.recordCount, result.duration)
                result.recordCount
            }
            is OperationResult.Error -> {
                logError("INSERT", result.error)
                throw RuntimeException("Bulk insert failed for $tableName: ${result.error}")
            }
        }
    }

    /**
     * Clear table ve sonra bulk INSERT
     * Transaction coordinator sayesinde atomic operation
     * Clear yapılacakmı Haluk ile görüş
     */
    override suspend fun clearAndInsert(items: List<T>): Int {
        val operation = buildDatabaseOperation(items, OperationType.CLEAR_AND_INSERT)

        return when (val result = coordinator.executeBulkOperation(operation)) {
            is OperationResult.Success -> {
                logSuccess("CLEAR_AND_INSERT", result.recordCount, result.duration)
                result.recordCount
            }
            is OperationResult.Error -> {
                logError("CLEAR_AND_INSERT", result.error)
                throw RuntimeException("Clear and insert failed for $tableName: ${result.error}")
            }
        }
    }

    /**
     * Bulk UPSERT operation (INSERT OR REPLACE)
     * Existing records update, new records insert
     * Update Insert olacak mı haluk ile görüs
     */
    override suspend fun upsertBatch(items: List<T>): Int {
        if (items.isEmpty()) return 0

        val operation = buildDatabaseOperation(items, OperationType.UPSERT)

        return when (val result = coordinator.executeBulkOperation(operation)) {
            is OperationResult.Success -> {
                logSuccess("UPSERT", result.recordCount, result.duration)
                result.recordCount
            }
            is OperationResult.Error -> {
                logError("UPSERT", result.error)
                throw RuntimeException("Bulk upsert failed for $tableName: ${result.error}")
            }
        }
    }

    //endregion

    //region Protected Method
    protected abstract val tableName: String
    protected abstract val insertColumns: List<String>
    protected abstract val clearSql: String
    protected abstract fun getValues(item: T): List<Any?>
    //endregion

    //region Private Method
    private fun buildDatabaseOperation(items: List<T>, type: OperationType): DatabaseOperation {
        // Optimal chunk size - SQL length limit ve performance balance
        val chunkSize = when (insertColumns.size) {
            in 1..3 -> 75    // Az column → büyük chunk
            in 4..6 -> 50    // Orta column → orta chunk
            else -> 25       // Çok column → küçük chunk
        }

        val chunkedValues = items.chunked(chunkSize).map { chunk ->
            buildValuesClause(chunk)
        }

        return DatabaseOperation(
            type = type,
            tableName = tableName,
            columns = insertColumns,
            values = chunkedValues,
            clearSql = clearSql,
            recordCount = items.size
        )
    }

    /**
     * VALUES clause builder
     * getValues() method'unu kullanarak her item için SQL values üretir
     * Bunu yeni yaptıgım orm den insan mudahalesine kalmadan alacagım
     *
     */
    private fun buildValuesClause(chunk: List<T>): String {
        return chunk.joinToString(", ") { item ->
            val values = getValues(item) // ← Alt sınıfın getValues() method'u çağrılır
            val escapedValues = values.mapIndexed { index, value ->
                formatSqlValue(value, index)
            }
            "(${escapedValues.joinToString(", ")})"
        }
    }

    /**
     * SQL value formatting - type'a göre quote vs no-quote
     */
    private fun formatSqlValue(value: Any?, columnIndex: Int): String {
        return when (value) {
            null -> "NULL"
            is Number -> value.toString() // Numbers - quote yok
            is Boolean -> if (value) "1" else "0" // Boolean - 1/0
            else -> "'${escapeSql(value.toString())}'" // Strings - quote var
        }
    }

    /**
     * SQL injection korunması
     * Single quote'ları escape eder
     */
    private fun escapeSql(value: String): String {
        return value.replace("'", "''")
    }

    /**
     * Success logging
     */
    private fun logSuccess(operation: String, recordCount: Int, duration: Long) {
        val ratePerSecond = if (duration > 0) (recordCount * 1000 / duration) else 0
        println("✅ Bulk $operation completed: $recordCount $tableName in ${duration}ms ($ratePerSecond rec/sec)")
    }

    /**
     * Error logging
     */
    private fun logError(operation: String, error: String) {
        println("❌ Bulk $operation failed for $tableName: $error")
    }

    /**
     * Debug helper - generated SQL preview
     */
    fun previewSql(items: List<T>, operation: OperationType = OperationType.INSERT): String {
        if (items.isEmpty()) return "-- No items to process"

        val sampleChunk = items.take(3) // İlk 3 item ile örnek
        val valuesClause = buildValuesClause(sampleChunk)

        val operationSql = when (operation) {
            OperationType.INSERT -> "INSERT"
            OperationType.UPSERT -> "INSERT OR REPLACE"
            OperationType.CLEAR_AND_INSERT -> "INSERT" // Clear ayrı SQL
        }

        val columns = insertColumns.joinToString(", ")
        val sql = "$operationSql INTO $tableName ($columns) VALUES $valuesClause"

        return if (items.size > 3) {
            "$sql\n-- ... and ${items.size - 3} more records"
        } else {
            sql
        }
    }
    //endregion
}