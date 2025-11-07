package com.repzone.sync.transaction

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.rawExecute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.ExperimentalTime

/**
 * Tüm bulk insertleri sirali olacak sekilde isler
 */
class TransactionCoordinator(
    private val iDatabaseManager: IDatabaseManager,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
    //region Field
    companion object {
        private val operationIdMutex = Mutex()
        private var operationIdCounter = 0L

        private suspend fun generateOperationId(): Long {
            return operationIdMutex.withLock {
                ++operationIdCounter
            }
        }
    }
    private val operationQueue = Channel<DatabaseOperation>(capacity = Channel.UNLIMITED)
    private val compositeQueue = Channel<CompositeOperation>(capacity = Channel.UNLIMITED)
    private val compositeResultChannels = mutableMapOf<Long, Channel<OperationResult>>()
    // Result channels for each operation
    private val resultChannels = mutableMapOf<Long, Channel<OperationResult>>()

    private val statsMutex = Mutex()
    private var totalOperations = 0L
    private var successfulOperations = 0L
    private var failedOperations = 0L
    //endregion

    //region Properties
    //endregion

    //region Constructor
    init {
        scope.launch {
            println("TransactionCoordinator started")

            for (operation in operationQueue) {
                processOperation(operation)
            }
        }

        scope.launch {
            for (compositeOp in compositeQueue) {
                processCompositeOperation(compositeOp)
            }
        }
    }
    //endregion

    //region Public Method
    suspend fun executeCompositeOperation(compositeOp: CompositeOperation): OperationResult {
        val operationId = generateOperationId()
        compositeOp.id = operationId

        val resultChannel = Channel<OperationResult>(1)
        compositeResultChannels[operationId] = resultChannel

        compositeQueue.send(compositeOp)
        val result = resultChannel.receive()
        compositeResultChannels.remove(operationId)

        return result
    }

    /**
     * Bulk operation'ı queue'ya ekler ve sonucu bekler
     * Thread-safe, multiple job'lar aynı anda çağırabilir
     */
    suspend fun executeBulkOperation(operation: DatabaseOperation): OperationResult {
        val operationId = generateOperationId()
        operation.id = operationId

        // Result channel oluştur
        val resultChannel = Channel<OperationResult>(1)
        resultChannels[operationId] = resultChannel

        // Queue'ya ekle
        operationQueue.send(operation)

        // Sonucu bekle
        val result = resultChannel.receive()

        // Cleanup
        resultChannels.remove(operationId)

        return result
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    /**
     * Operation'ı işler - TEK THREAD'DE SIRAYLA
     */
    @OptIn(ExperimentalTime::class)
    private suspend fun processOperation(operation: DatabaseOperation) {
        val startTime = now()

        // Stats update
        statsMutex.withLock {
            totalOperations++
        }

        try {
            val result = iDatabaseManager.getDatabase().transactionWithResult {
                when (operation.type) {
                    OperationType.CLEAR_AND_INSERT -> {
                        // Önce temizle
                        executeRawSql(operation.clearSql)
                        // Sonra bulk insert
                        executeBulkInsert(operation)
                    }
                    OperationType.INSERT -> {
                        executeBulkInsert(operation)
                    }
                    OperationType.UPSERT -> {
                        executeBulkUpsert(operation)
                    }
                }
                operation.recordCount
            }

            val duration = now() - startTime

            // Stats update
            statsMutex.withLock {
                successfulOperations++
            }

            val successResult = OperationResult.Success(
                recordCount = result,
                duration = duration,
                operationId = operation.id
            )

            println("Operation completed: ${operation.type} ${operation.tableName} - $result records in ${duration}ms")

            // Result'ı gönder
            resultChannels[operation.id]?.send(successResult)

        } catch (e: Exception) {
            val duration = now() - startTime

            // Stats update
            statsMutex.withLock {
                failedOperations++
            }

            val errorResult = OperationResult.Error(
                error = e.message ?: "Unknown error",
                duration = duration,
                operationId = operation.id
            )

            println("Operation failed: ${operation.type} ${operation.tableName} - ${e.message}")

            // Error result'ı gönder
            resultChannels[operation.id]?.send(errorResult)
        }
    }

    private fun executeRawSql(sql: String) {
        try {
            runBlocking {
                iDatabaseManager.getSqlDriver().rawExecute (sql)
            }
        }catch (ex: Exception){
            Logger.error(ex)
            throw ex;
        }

    }

    private fun executeBulkInsert(operation: DatabaseOperation) {
        val sql = buildBulkInsertSql(operation)
        executeRawSql(sql)
    }

    private fun executeBulkUpsert(operation: DatabaseOperation) {
        val sql = buildBulkUpsertSql(operation)
        executeRawSql(sql)
    }

    private fun buildBulkInsertSql(operation: DatabaseOperation): String {
        val columns = operation.columns.joinToString(", ")
        val values = operation.values.joinToString(", ")
        return "INSERT INTO ${operation.tableName} ($columns) VALUES $values"
    }

    private fun buildBulkUpsertSql(operation: DatabaseOperation): String {
        val columns = operation.columns.joinToString(", ") { "`$it`" }
        val values = operation.values.joinToString(", ")
        return "INSERT OR REPLACE INTO ${operation.tableName} ($columns) VALUES $values"
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun processCompositeOperation(compositeOp: CompositeOperation) {
        val startTime = now()

        statsMutex.withLock {
            totalOperations++
        }

        try {
            val totalRecords = iDatabaseManager.getDatabase().transactionWithResult {
                var recordCount = 0

                compositeOp.operations.forEach { tableOp ->
                    if (tableOp.recordCount <= 0) {
                        return@forEach
                    }
                    if(tableOp.includeClears && tableOp.clearSql != null && tableOp.clearSql != ""){
                        executeRawSql(tableOp.clearSql)
                    }

                    val sql = if(tableOp.useUpsert){
                        buildBulkUpsertSqlFromTableOp(tableOp)
                    }else{
                        buildBulkInsertSqlFromTableOp(tableOp)
                    }
                    executeRawSql(sql)
                    if(tableOp.includeOtherTableCount){
                        recordCount += tableOp.recordCount
                    }
                }

                recordCount
            }

            val duration = now() - startTime

            statsMutex.withLock {
                successfulOperations++
            }

            val successResult = OperationResult.Success(
                recordCount = totalRecords,
                duration = duration,
                operationId = compositeOp.id
            )
            compositeResultChannels[compositeOp.id]?.send(successResult)

        } catch (e: Exception) {
            val duration = now() - startTime

            statsMutex.withLock {
                failedOperations++
            }

            val errorResult = OperationResult.Error(
                error = e.message ?: "Unknown error",
                duration = duration,
                operationId = compositeOp.id
            )

            println("Composite operation failed: ${compositeOp.description} - ${e.message}")

            compositeResultChannels[compositeOp.id]?.send(errorResult)
        }
    }

    private fun buildBulkInsertSqlFromTableOp(tableOp: TableOperation): String {
        val columns = tableOp.columns.joinToString(", ")
        val values = tableOp.values.joinToString(", ")
        return "INSERT INTO ${tableOp.tableName} ($columns) VALUES $values"
    }

    private fun buildBulkUpsertSqlFromTableOp(tableOp: TableOperation): String {
        val columns = tableOp.columns.joinToString(", ")
        val values = tableOp.values.joinToString(", ")
        return "INSERT OR REPLACE INTO ${tableOp.tableName} ($columns) VALUES $values"
    }

    /**
     * Statistics - Thread-safe
     */
    suspend fun getStats(): TransactionStats {
        return statsMutex.withLock {
            TransactionStats(
                totalOperations = totalOperations,
                successfulOperations = successfulOperations,
                failedOperations = failedOperations,
                queueSize = 0 // Approximate
            )
        }
    }

    fun shutdown() {
        operationQueue.close()
        compositeQueue.close()
        scope.cancel()
        println("TransactionCoordinator shutdown")
    }
    //endregion
}