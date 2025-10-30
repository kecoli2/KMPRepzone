package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.QueryResult

// Raw SQL query - Generic result
fun SqlDriver.rawQuery(
    sql: String,
    vararg params: Any?
): List<Map<String, Any?>> {
    val results = mutableListOf<Map<String, Any?>>()

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            while (cursor.next().value) {
                val row = mutableMapOf<String, Any?>()

                // Her kolonu oku (column count bilinmiyor, try-catch ile bul)
                var columnIndex = 0
                while (true) {
                    try {
                        val value = cursor.getString(columnIndex)
                        row["column_$columnIndex"] = value
                        columnIndex++
                    } catch (e: Exception) {
                        break
                    }
                }

                results.add(row)
            }
            QueryResult.Value(results)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return results
}

// Raw SQL query - Entity result
inline fun <reified T : Any> SqlDriver.rawQueryToEntity(
    sql: String,
    vararg params: Any?
): List<T> {
    val metadata = EntityMetadataRegistry.get<T>()
    val results = mutableListOf<T>()

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            while (cursor.next().value) {
                val entity = metadata.createInstance(SqlDelightCursor(cursor)) as T
                results.add(entity)
            }
            QueryResult.Value(results)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return results
}

// Raw SQL execute (INSERT/UPDATE/DELETE)
fun SqlDriver.rawExecute(
    sql: String,
    vararg params: Any?
): Long {
    return execute(
        identifier = null,
        sql = sql,
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }.value
}

// Raw SQL with single result
fun SqlDriver.rawQueryScalar(
    sql: String,
    vararg params: Any?
): Any? {
    var result: Any? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = cursor.getString(0)
            }
            QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

// Raw SQL COUNT
fun SqlDriver.rawCount(
    sql: String,
    vararg params: Any?
): Long {
    var count = 0L

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                count = cursor.getLong(0) ?: 0L
            }
            QueryResult.Value(count)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return count
}

// Raw SQL SUM/AVG/MAX/MIN
fun SqlDriver.rawAggregate(
    sql: String,
    vararg params: Any?
): Double {
    var result = 0.0

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = cursor.getDouble(0) ?: 0.0
            }
            QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

// Raw SQL - First or null
inline fun <reified T : Any> SqlDriver.rawQueryFirstOrNull(
    sql: String,
    vararg params: Any?
): T? {
    return rawQueryToEntity<T>(sql, *params).firstOrNull()
}

// Raw SQL transaction
fun SqlDriver.rawTransaction(block: RawSqlScope.() -> Unit) {
    execute(null, "BEGIN TRANSACTION", 0)

    try {
        val scope = RawSqlScope(this)
        scope.block()
        execute(null, "COMMIT", 0)
    } catch (e: Exception) {
        execute(null, "ROLLBACK", 0)
        throw e
    }
}

// Raw SQL scope
class RawSqlScope(val driver: SqlDriver) {

    fun execute(sql: String, vararg params: Any?): Long {
        return driver.rawExecute(sql, *params)
    }

    fun <T : Any> query(sql: String, vararg params: Any?): List<Map<String, Any?>> {
        return driver.rawQuery(sql, *params)
    }

    inline fun <reified T : Any> queryToEntity(sql: String, vararg params: Any?): List<T> {
        return driver.rawQueryToEntity(sql, *params)
    }

    fun scalar(sql: String, vararg params: Any?): Any? {
        return driver.rawQueryScalar(sql, *params)
    }
}