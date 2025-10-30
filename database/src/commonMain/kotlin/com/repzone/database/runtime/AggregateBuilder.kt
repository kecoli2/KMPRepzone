package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

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

    val sql = "SELECT SUM($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT AVG($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MAX($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MAX($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MAX($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MIN($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MIN($field) FROM ${metadata.tableName}$whereClause"

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

    val sql = "SELECT MIN($field) FROM ${metadata.tableName}$whereClause"

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