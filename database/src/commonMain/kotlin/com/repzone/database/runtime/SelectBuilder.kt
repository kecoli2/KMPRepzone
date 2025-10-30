package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

class SelectBuilder<T>(private val metadata: EntityMetadata, private val driver: SqlDriver) {
    var whereCondition: Condition = NoCondition
    internal var orderSpecs: List<OrderSpec> = emptyList()
    internal var groupByBuilder: GroupByBuilder? = null
    internal var limitValue: Int? = null

    fun where(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        whereCondition = builder.build()
    }

    fun orderBy(block: OrderByBuilder.() -> Unit) {
        val builder = OrderByBuilder()
        builder.block()
        orderSpecs = builder.build()
    }

    fun limit(count: Int) {
        require(count > 0) { "Limit must be positive" }
        limitValue = count
    }

    fun groupBy(block: GroupByBuilder.() -> Unit) {
        val builder = GroupByBuilder()
        builder.block()
        groupByBuilder = builder
    }

    fun toList(): List<T> {
        val params = mutableListOf<Any?>()

        // Build WHERE clause
        val whereClause = if (whereCondition != NoCondition) {
            " WHERE ${whereCondition.toSQL(params)}"
        } else {
            ""
        }

        // Build GROUP BY clause
        val groupByClause = groupByBuilder?.let { builder ->
            if (builder.groupByFields.isNotEmpty()) {
                " GROUP BY ${builder.groupByFields.joinToString(", ")}"
            } else {
                ""
            }
        } ?: ""

        // Build HAVING clause
        val havingClause = groupByBuilder?.let { builder ->
            if (builder.havingCondition != NoCondition) {
                " HAVING ${builder.havingCondition.toSQL(params)}"
            } else {
                ""
            }
        } ?: ""

        // Build ORDER BY clause
        val orderByClause = if (orderSpecs.isNotEmpty()) {
            " ORDER BY " + orderSpecs.joinToString(", ") { spec ->
                "${spec.field} ${spec.direction.name}"
            }
        } else {
            ""
        }

        // Build LIMIT clause
        val limitClause = limitValue?.let { " LIMIT $it" } ?: ""

        // Final SQL
        val sql = "SELECT ${metadata.columns.joinToString(", ") { it.name }} " +
                "FROM ${metadata.tableName}" +
                whereClause +
                groupByClause +
                havingClause +
                orderByClause +
                limitClause

        // Execute and collect results
        val results = mutableListOf<T>()

        driver.executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                // Cursor'u iterate et - HER SATIR için döngü
                while (cursor.next().value) {
                    val entity = metadata.createInstance(SqlDelightCursor(cursor)) as T
                    results.add(entity)
                }
                app.cash.sqldelight.db.QueryResult.Value(results)  // Unit değil, Value döndür
            },
            parameters = params.size
        ) {
            if (params.isNotEmpty()) {
                params.forEachIndexed { index, value ->
                    bindValue(this, index, value)
                }
            }
        }

        return results
    }

    fun firstOrNull(): T? {
        limit(1)
        val params = mutableListOf<Any?>()

        val whereClause = if (whereCondition != NoCondition) {
            " WHERE ${whereCondition.toSQL(params)}"
        } else {
            ""
        }

        // Build GROUP BY clause
        val groupByClause = groupByBuilder?.let { builder ->
            if (builder.groupByFields.isNotEmpty()) {
                " GROUP BY ${builder.groupByFields.joinToString(", ")}"
            } else {
                ""
            }
        } ?: ""

        // Build HAVING clause
        val havingClause = groupByBuilder?.let { builder ->
            if (builder.havingCondition != NoCondition) {
                " HAVING ${builder.havingCondition.toSQL(params)}"
            } else {
                ""
            }
        } ?: ""

        val sql = "SELECT ${metadata.columns.joinToString(", ") { it.name }} " +
                "FROM ${metadata.tableName}" +
                whereClause +
                groupByClause +
                havingClause +
                " LIMIT 1"

        var result: T? = null

        driver.executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                if (cursor.next().value) {  // İlk satıra git
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

    fun first(): T {
        return firstOrNull() ?: throw NoSuchElementException("No entity found")
    }

}

// SQLDelight Cursor wrapper
private class SqlDelightCursor(
    private val cursor: app.cash.sqldelight.db.SqlCursor
) : Cursor {
    override fun getString(index: Int): String? = cursor.getString(index)
    override fun getLong(index: Int): Long? = cursor.getLong(index)
    override fun getDouble(index: Int): Double? = cursor.getDouble(index)
    override fun getBytes(index: Int): ByteArray? = cursor.getBytes(index)
    override fun getBoolean(index: Int): Boolean? = cursor.getLong(index)?.let { it != 0L }
}