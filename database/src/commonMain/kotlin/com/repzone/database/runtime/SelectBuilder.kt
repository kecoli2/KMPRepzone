package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

class SelectBuilder<T>(private val metadata: EntityMetadata, private val driver: SqlDriver) {
    private var whereCondition: Condition = NoCondition
    private var orderSpecs: List<OrderSpec> = emptyList()
    private var limitValue: Int? = null

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

    fun toList(): List<T> {
        val params = mutableListOf<Any?>()

        // Build WHERE clause
        val whereClause = if (whereCondition != NoCondition) {
            " WHERE ${whereCondition.toSQL(params)}"
        } else {
            ""
        }

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
                whereClause + orderByClause + limitClause

        // Execute and collect results
        val results = mutableListOf<T>()

        driver.executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                results.add(metadata.createInstance(SqlDelightCursor(cursor)) as T)
                app.cash.sqldelight.db.QueryResult.Unit
            },
            parameters = params.size
        ){
            params.forEachIndexed { index, value ->
                bindValue(this, index + 1, value)
            }
        }

        return results
    }

    fun firstOrNull(): T? = toList().firstOrNull()

    fun first(): T = toList().first()

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