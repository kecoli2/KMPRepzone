@file:OptIn(ExperimentalTime::class)

package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

// COUNT
inline fun <reified T : Any> SqlDriver.count(
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Long {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT COUNT(*) FROM ${metadata.tableName}$whereClause"

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var count = 0L

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    count = cursor.getLong(0) ?: 0L
                }
                app.cash.sqldelight.db.QueryResult.Value(count)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("COUNT", elapsed)
        Logger.d("SQL_RESULT", "Count: $count")

        return count
    }

    var count = 0L

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                count = cursor.getLong(0) ?: 0L
            }
            app.cash.sqldelight.db.QueryResult.Value(count)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return count
}

// SUM
inline fun <reified T : Any> SqlDriver.sum(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Double {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT SUM(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var sum = 0.0

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    sum = cursor.getDouble(0) ?: 0.0
                }
                app.cash.sqldelight.db.QueryResult.Value(sum)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("SUM", elapsed)
        Logger.d("SQL_RESULT", "Sum: $sum")

        return sum
    }

    var sum = 0.0

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                sum = cursor.getDouble(0) ?: 0.0
            }
            app.cash.sqldelight.db.QueryResult.Value(sum)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return sum
}

// AVG
inline fun <reified T : Any> SqlDriver.avg(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Double {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT AVG(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var avg = 0.0

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    avg = cursor.getDouble(0) ?: 0.0
                }
                app.cash.sqldelight.db.QueryResult.Value(avg)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("AVG", elapsed)
        Logger.d("SQL_RESULT", "Average: $avg")

        return avg
    }

    var avg = 0.0

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                avg = cursor.getDouble(0) ?: 0.0
            }
            app.cash.sqldelight.db.QueryResult.Value(avg)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return avg
}

// MAX - Long için
inline fun <reified T : Any> SqlDriver.maxLong(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Long? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MAX(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var max: Long? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    max = cursor.getLong(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(max)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MAX", elapsed)
        Logger.d("SQL_RESULT", "Max: $max")

        return max
    }

    var max: Long? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                max = cursor.getLong(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(max)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return max
}

// MAX - Double için
inline fun <reified T : Any> SqlDriver.maxDouble(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Double? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MAX(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var max: Double? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    max = cursor.getDouble(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(max)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MAX", elapsed)

        return max
    }

    var max: Double? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                max = cursor.getDouble(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(max)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return max
}

// MAX - String için
inline fun <reified T : Any> SqlDriver.maxString(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): String? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MAX(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var max: String? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    max = cursor.getString(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(max)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MAX", elapsed)

        return max
    }

    var max: String? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                max = cursor.getString(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(max)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return max
}

// MIN - Long için
inline fun <reified T : Any> SqlDriver.minLong(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Long? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MIN(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var min: Long? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    min = cursor.getLong(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(min)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MIN", elapsed)
        Logger.d("SQL_RESULT", "Min: $min")

        return min
    }

    var min: Long? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                min = cursor.getLong(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(min)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return min
}

// MIN - Double için
inline fun <reified T : Any> SqlDriver.minDouble(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Double? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MIN(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var min: Double? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    min = cursor.getDouble(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(min)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MIN", elapsed)

        return min
    }

    var min: Double? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                min = cursor.getDouble(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(min)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return min
}

// MIN - String için
inline fun <reified T : Any> SqlDriver.minString(
    field: String,
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): String? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val sql = "SELECT MIN(${field.escapeIfReserved()}) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var min: String? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    min = cursor.getString(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(min)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MIN", elapsed)

        return min
    }

    var min: String? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                min = cursor.getString(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(min)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return min
}