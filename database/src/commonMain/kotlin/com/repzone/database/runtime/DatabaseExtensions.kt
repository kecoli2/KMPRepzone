package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

// ========================================
// SqlDriver Extensions (Ana Kullanım)
// ========================================

inline fun <reified T : Any> SqlDriver.select(
    block: SelectBuilder<T>.() -> Unit = {}
): SelectBuilder<T> {
    val metadata = EntityMetadataRegistry.get<T>()
    return SelectBuilder<T>(metadata, this).apply(block)
}

fun SqlDriver.insert(entity: Any): Long = insertGenerated(entity)

fun SqlDriver.update(entity: Any): Int = updateGenerated(entity)

fun SqlDriver.delete(entity: Any): Int = deleteGenerated(entity)

inline fun <reified T : Any> SqlDriver.delete(
    block: DeleteBuilder<T>.() -> Unit
): Int {
    val metadata = EntityMetadataRegistry.get<T>()
    val builder = DeleteBuilder<T>(metadata, this)
    builder.block()
    return builder.execute()
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