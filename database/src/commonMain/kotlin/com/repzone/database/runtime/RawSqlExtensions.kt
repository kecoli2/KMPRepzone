@file:OptIn(ExperimentalTime::class)

package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.QueryResult
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.database.runtime.SqlKeywordEscaper.escapeKeywordsInQuery
import kotlin.time.ExperimentalTime

// Raw SQL query - Generic result
fun SqlDriver.rawQuery(
    sql: String,
    vararg params: Any?
): List<Map<String, Any?>> {
    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params.toList())

        val results = mutableListOf<Map<String, Any?>>()

        executeQuery(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW QUERY", elapsed)
        Logger.d("SQL_RESULT", "Returned ${results.size} rows")

        return results
    }

    val results = mutableListOf<Map<String, Any?>>()

    executeQuery(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
        mapper = { cursor ->
            while (cursor.next().value) {
                val row = mutableMapOf<String, Any?>()

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

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql.escapeKeywordsInQuery(), params.toList())

        val results = mutableListOf<T>()

        executeQuery(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW QUERY TO ENTITY", elapsed)
        Logger.d("SQL_RESULT", "Returned ${results.size} entities")

        return results
    }

    val results = mutableListOf<T>()

    executeQuery(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
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
    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql.escapeKeywordsInQuery(), params.toList())

        val result = execute(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }.value

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW EXECUTE", elapsed)
        Logger.d("SQL_RESULT", "Affected: $result")

        return result
    }

    return execute(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
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
    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql.escapeKeywordsInQuery(), params.toList())

        var result: Any? = null

        executeQuery(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW QUERY SCALAR", elapsed)
        Logger.d("SQL_RESULT", "Result: $result")

        return result
    }

    var result: Any? = null

    executeQuery(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
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
    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql.escapeKeywordsInQuery(), params.toList())

        var count = 0L

        executeQuery(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW COUNT", elapsed)
        Logger.d("SQL_RESULT", "Count: $count")

        return count
    }

    var count = 0L

    executeQuery(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
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
    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql.escapeKeywordsInQuery(), params.toList())

        var result = 0.0

        executeQuery(
            identifier = null,
            sql = sql.escapeKeywordsInQuery(),
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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("RAW AGGREGATE", elapsed)
        Logger.d("SQL_RESULT", "Result: $result")

        return result
    }

    var result = 0.0

    executeQuery(
        identifier = null,
        sql = sql.escapeKeywordsInQuery(),
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
    if (BuildConfig.IS_DEBUG) {
        SqlQueryLogger.logTransactionStart()
        val startTime = now()

        execute(null, "BEGIN TRANSACTION", 0)

        try {
            val scope = RawSqlScope(this)
            scope.block()
            execute(null, "COMMIT", 0)

            val elapsed = (now() - startTime).toInstant().epochSeconds
            SqlQueryLogger.logTransactionCommit()
            SqlQueryLogger.logQueryTime("RAW TRANSACTION", elapsed)
        } catch (e: Exception) {
            execute(null, "ROLLBACK", 0)
            SqlQueryLogger.logTransactionRollback(e)
            throw e
        }

        return
    }

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