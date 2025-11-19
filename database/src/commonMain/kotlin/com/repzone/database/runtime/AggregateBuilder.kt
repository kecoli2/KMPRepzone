@file:OptIn(ExperimentalTime::class)

package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

// COUNT
inline fun <reified T : Any> SqlDriver.count(noinline block: (SelectBuilder<T>.() -> Unit)? = null): Long {
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
inline fun <reified T : Any> SqlDriver.sum(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Double {
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
inline fun <reified T : Any> SqlDriver.avg(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Double {
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
inline fun <reified T : Any> SqlDriver.maxLong(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Long? {
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
inline fun <reified T : Any> SqlDriver.maxDouble(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Double? {
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
inline fun <reified T : Any> SqlDriver.maxString(field: String,noinline block: (SelectBuilder<T>.() -> Unit)? = null): String? {
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
inline fun <reified T : Any> SqlDriver.minLong(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Long? {
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
inline fun <reified T : Any> SqlDriver.minDouble(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Double? {
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
inline fun <reified T : Any> SqlDriver.minString(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): String? {
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

// MAXORNULL
inline fun <reified T : Any> SqlDriver.maxByOrNull(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): T? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)

    val columnNames = metadata.columns.joinToString(", ") { it.name }
    val sql = "SELECT $columnNames FROM ${metadata.tableName}$whereClause ORDER BY $escapedField DESC LIMIT 1"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: T? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = metadata.createInstance(SqlDelightCursor(cursor)) as T
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MAX_BY", elapsed)
        Logger.d("SQL_RESULT", "Result: ${if (result != null) "Found" else "null"}")

        return result
    }

    var result: T? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = metadata.createInstance(SqlDelightCursor(cursor)) as T
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

// MINORNULL
inline fun <reified T : Any> SqlDriver.minByOrNull(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): T? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)

    val columnNames = metadata.columns.joinToString(", ") { it.name }
    val sql = "SELECT $columnNames FROM ${metadata.tableName}$whereClause ORDER BY $escapedField ASC LIMIT 1"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: T? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = metadata.createInstance(SqlDelightCursor(cursor)) as T
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MIN_BY", elapsed)
        Logger.d("SQL_RESULT", "Result: ${if (result != null) "Found" else "null"}")

        return result
    }

    var result: T? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = metadata.createInstance(SqlDelightCursor(cursor)) as T
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

inline fun <reified T : Any, reified R : Number> SqlDriver.maxOfOrNull(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): R? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)
    val sql = "SELECT MAX($escapedField) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: R? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = when (R::class) {
                        Long::class -> cursor.getLong(0) as? R
                        Int::class -> cursor.getLong(0)?.toInt() as? R
                        Double::class -> cursor.getDouble(0) as? R
                        Float::class -> cursor.getDouble(0)?.toFloat() as? R
                        else -> cursor.getLong(0) as? R
                    }
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MAX_OF", elapsed)
        Logger.d("SQL_RESULT", "Max: $result")

        return result
    }

    var result: R? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = when (R::class) {
                    Long::class -> cursor.getLong(0) as? R
                    Int::class -> cursor.getLong(0)?.toInt() as? R
                    Double::class -> cursor.getDouble(0) as? R
                    Float::class -> cursor.getDouble(0)?.toFloat() as? R
                    else -> cursor.getLong(0) as? R
                }
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

inline fun <reified T : Any, reified R : Number> SqlDriver.minOfOrNull(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): R? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)
    val sql = "SELECT MIN($escapedField) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: R? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = when (R::class) {
                        Long::class -> cursor.getLong(0) as? R
                        Int::class -> cursor.getLong(0)?.toInt() as? R
                        Double::class -> cursor.getDouble(0) as? R
                        Float::class -> cursor.getDouble(0)?.toFloat() as? R
                        else -> cursor.getLong(0) as? R
                    }
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("MIN_OF", elapsed)
        Logger.d("SQL_RESULT", "Min: $result")

        return result
    }

    var result: R? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = when (R::class) {
                    Long::class -> cursor.getLong(0) as? R
                    Int::class -> cursor.getLong(0)?.toInt() as? R
                    Double::class -> cursor.getDouble(0) as? R
                    Float::class -> cursor.getDouble(0)?.toFloat() as? R
                    else -> cursor.getLong(0) as? R
                }
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

inline fun <reified T : Any, reified R : Number> SqlDriver.sumOf(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): R {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)
    val sql = "SELECT SUM($escapedField) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: R = when (R::class) {
            Long::class -> 0L as R
            Int::class -> 0 as R
            Double::class -> 0.0 as R
            Float::class -> 0f as R
            else -> 0L as R
        }

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = when (R::class) {
                        Long::class -> (cursor.getLong(0) ?: 0L) as R
                        Int::class -> (cursor.getLong(0)?.toInt() ?: 0) as R
                        Double::class -> (cursor.getDouble(0) ?: 0.0) as R
                        Float::class -> (cursor.getDouble(0)?.toFloat() ?: 0f) as R
                        else -> (cursor.getLong(0) ?: 0L) as R
                    }
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("SUM_OF", elapsed)
        Logger.d("SQL_RESULT", "Sum: $result")

        return result
    }

    var result: R = when (R::class) {
        Long::class -> 0L as R
        Int::class -> 0 as R
        Double::class -> 0.0 as R
        Float::class -> 0f as R
        else -> 0L as R
    }

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = when (R::class) {
                    Long::class -> (cursor.getLong(0) ?: 0L) as R
                    Int::class -> (cursor.getLong(0)?.toInt() ?: 0) as R
                    Double::class -> (cursor.getDouble(0) ?: 0.0) as R
                    Float::class -> (cursor.getDouble(0)?.toFloat() ?: 0f) as R
                    else -> (cursor.getLong(0) ?: 0L) as R
                }
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

inline fun <reified T : Any> SqlDriver.averageOfOrNull(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): Double? {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)
    val sql = "SELECT AVG($escapedField) FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        var result: Double? = null

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {
                    result = cursor.getDouble(0)
                }
                app.cash.sqldelight.db.QueryResult.Value(result)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("AVERAGE_OF", elapsed)
        Logger.d("SQL_RESULT", "Average: $result")

        return result
    }

    var result: Double? = null

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            if (cursor.next().value) {
                result = cursor.getDouble(0)
            }
            app.cash.sqldelight.db.QueryResult.Value(result)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return result
}

inline fun <reified T : Any> SqlDriver.distinctBy(field: String, noinline block: (SelectBuilder<T>.() -> Unit)? = null): List<String> {
    val metadata = EntityMetadataRegistry.get<T>()
    val params = mutableListOf<Any?>()

    val builder = SelectBuilder<T>(metadata, this)
    block?.invoke(builder)

    val whereClause = if (builder.whereCondition != NoCondition) {
        " WHERE ${builder.whereCondition.toSQL(params)}"
    } else {
        ""
    }

    val escapedField = SqlKeywordEscaper.escapeColumnName(field)
    val sql = "SELECT DISTINCT $escapedField FROM ${metadata.tableName}$whereClause"

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logRawQuery(sql, params)

        val results = mutableListOf<String>()

        executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                while (cursor.next().value) {
                    cursor.getString(0)?.let { results.add(it) }
                }
                app.cash.sqldelight.db.QueryResult.Value(results)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("DISTINCT_BY", elapsed)
        Logger.d("SQL_RESULT", "Found ${results.size} distinct values")

        return results
    }

    val results = mutableListOf<String>()

    executeQuery(
        identifier = null,
        sql = sql,
        mapper = { cursor ->
            while (cursor.next().value) {
                cursor.getString(0)?.let { results.add(it) }
            }
            app.cash.sqldelight.db.QueryResult.Value(results)
        },
        parameters = params.size
    ) {
        params.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return results
}