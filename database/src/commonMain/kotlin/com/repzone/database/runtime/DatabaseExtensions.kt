@file:OptIn(ExperimentalTime::class)

package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

// ========================================
// SqlDriver Extensions (Ana Kullanım)
// ========================================

inline fun <reified T : Any> SqlDriver.select(
    block: SelectBuilder<T>.() -> Unit = {}
): SelectBuilder<T> {
    val metadata = EntityMetadataRegistry.get<T>()
    return SelectBuilder<T>(metadata, this).apply(block)
}

fun SqlDriver.insert(entity: Any): Long {
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entity)
        val values = metadata.extractValues(entity)
        val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }

        SqlQueryLogger.logInsert(
            tableName = metadata.tableName,
            columns = insertColumns.map { it.name },
            values = insertColumns.map { values[it.name] }
        )

        val result = insertGenerated(entity)

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("INSERT", elapsed)

        return result
    }

    return insertGenerated(entity)
}

fun SqlDriver.update(entity: Any): Int {
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entity)
        val values = metadata.extractValues(entity)
        val pk = metadata.primaryKey
        val updateColumns = metadata.columns.filterNot { it.isPrimaryKey }

        SqlQueryLogger.logUpdate(
            tableName = metadata.tableName,
            setColumns = updateColumns.map { it.name },
            whereClause = "${pk.name} = ?",
            parameters = updateColumns.map { values[it.name] } + listOf(values[pk.name])
        )

        val result = updateGenerated(entity)

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("UPDATE", elapsed)

        return result
    }

    return updateGenerated(entity)
}

fun SqlDriver.delete(entity: Any): Int {
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entity)
        val values = metadata.extractValues(entity)
        val pk = metadata.primaryKey

        SqlQueryLogger.logDelete(
            tableName = metadata.tableName,
            whereClause = "${pk.name} = ?",
            parameters = listOf(values[pk.name])
        )

        val result = deleteGenerated(entity)

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("DELETE", elapsed)

        return result
    }

    return deleteGenerated(entity)
}

fun SqlDriver.insertOrReplace(entity: Any): Long {
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entity)
        val values = metadata.extractValues(entity)
        val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }

        SqlQueryLogger.logRawQuery(
            "INSERT OR REPLACE INTO ${metadata.tableName} (${insertColumns.joinToString(", ") { it.name }}) VALUES (${insertColumns.joinToString(", ") { "?" }})",
            insertColumns.map { values[it.name] }
        )

        val result = insertOrReplaceGenerated(entity)

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("INSERT OR REPLACE", elapsed)

        return result
    }

    return insertOrReplaceGenerated(entity)
}

inline fun <reified T : Any> SqlDriver.delete(
    block: DeleteBuilder<T>.() -> Unit
): Int {
    val metadata = EntityMetadataRegistry.get<T>()
    val builder = DeleteBuilder<T>(metadata, this)
    builder.block()

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()

        val params = mutableListOf<Any?>()
        val whereClause = builder.getWhereClause(params)

        SqlQueryLogger.logDelete(
            tableName = metadata.tableName,
            whereClause = whereClause,
            parameters = params
        )

        val result = builder.execute()

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("DELETE", elapsed)

        return result
    }

    return builder.execute()
}

// ========================================
// Batch Operations with Logging
// ========================================

fun SqlDriver.batchInsert(entities: List<Any>): List<Long> {
    if (entities.isEmpty()) return emptyList()

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())

        SqlQueryLogger.logBatch("INSERT", metadata.tableName, entities.size)

        val results = entities.map { insertGenerated(it) }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT", elapsed)

        return results
    }

    return entities.map { insertGenerated(it) }
}

fun SqlDriver.batchUpdate(entities: List<Any>): Int {
    if (entities.isEmpty()) return 0

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())

        SqlQueryLogger.logBatch("UPDATE", metadata.tableName, entities.size)

        val results = entities.sumOf { updateGenerated(it) }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH UPDATE", elapsed)

        return results
    }

    return entities.sumOf { updateGenerated(it) }
}

fun SqlDriver.batchDelete(entities: List<Any>): Int {
    if (entities.isEmpty()) return 0

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())

        SqlQueryLogger.logBatch("DELETE", metadata.tableName, entities.size)

        val results = entities.sumOf { deleteGenerated(it) }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH DELETE", elapsed)

        return results
    }

    return entities.sumOf { deleteGenerated(it) }
}

// ========================================
// Transaction with Logging
// ========================================

fun <R> SqlDriver.transaction(block: TransactionScope.() -> R): R {
    if (BuildConfig.IS_DEBUG) {
        SqlQueryLogger.logTransactionStart()
        val startTime = now()

        return try {
            val result = transactionWithResult {
                TransactionScope(this@transaction).block()
            }

            val elapsed = (now() - startTime).toInstant().epochSeconds
            SqlQueryLogger.logTransactionCommit()
            SqlQueryLogger.logQueryTime("TRANSACTION", elapsed)

            result
        } catch (e: Throwable) {
            SqlQueryLogger.logTransactionRollback(e)
            throw e
        }
    }

    return transactionWithResult {
        TransactionScope(this@transaction).block()
    }
}

// ========================================
// DeleteBuilder
// ========================================

class DeleteBuilder<T : Any>(
    private val metadata: EntityMetadata,
    private val driver: SqlDriver
) {
    private var whereCondition: Condition = NoCondition

    fun where(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        whereCondition = builder.build()
    }

    fun getWhereClause(params: MutableList<Any?>): String {
        return if (whereCondition != NoCondition) {
            whereCondition.toSQL(params)
        } else {
            error("WHERE clause is required for delete with criteria")
        }
    }

    fun execute(): Int {
        val params = mutableListOf<Any?>()

        val whereClause = if (whereCondition != NoCondition) {
            " WHERE ${whereCondition.toSQL(params)}"
        } else {
            error("WHERE clause is required for delete with criteria")
        }

        val sql = "DELETE FROM ${metadata.tableName}$whereClause"

        return driver.execute(
            identifier = null,
            sql = sql,
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }.value.toInt()
    }
}

// ========================================
// Helper Functions
// ========================================

fun bindValue(statement: app.cash.sqldelight.db.SqlPreparedStatement, index: Int, value: Any?) {
    when (value) {
        null -> statement.bindBytes(index, null)
        is String -> statement.bindString(index, value)
        is Long -> statement.bindLong(index, value)
        is Int -> statement.bindLong(index, value.toLong())
        is Boolean -> statement.bindLong(index, if (value) 1L else 0L)
        is Double -> statement.bindDouble(index, value)
        is Float -> statement.bindDouble(index, value.toDouble())
        is ByteArray -> statement.bindBytes(index, value)
        else -> statement.bindString(index, value.toString())
    }
}
