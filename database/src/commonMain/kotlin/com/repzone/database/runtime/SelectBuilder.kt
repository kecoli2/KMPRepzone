package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SelectBuilder<T>(val metadata: EntityMetadata, private val driver: SqlDriver, private val baseSql: String? = null) {
    var whereCondition: Condition = NoCondition
        internal set

    var orderSpecs = mutableListOf<OrderSpec>()
        internal set

    var limitValue: Int? = null
        internal set

    var offsetValue: Int? = null
        internal set

    var groupByBuilder: GroupByBuilder? = null
        internal set

    val joins = mutableListOf<JoinConfig>()


    fun where(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        whereCondition = builder.build()
    }

    fun orderBy(block: OrderByBuilder.() -> Unit) {
        val builder = OrderByBuilder()
        builder.block()
        orderSpecs = builder.orderSpecs
    }

    fun limit(count: Int) {
        require(count > 0) { "Limit must be positive" }
        limitValue = count
    }

    fun offset(count: Int) {
        offsetValue = count
    }

    fun groupBy(block: GroupByBuilder.() -> Unit) {
        val builder = GroupByBuilder()
        builder.block()
        groupByBuilder = builder
    }

    /**
     * Build SQL query string (for logging)
     */
    private fun buildSQL(params: MutableList<Any?>): String {
        // Raw SQL varsa onu base olarak kullan
        if (baseSql != null) {
            return buildFromRawSQL(params)
        }

        // Normal SELECT build (mevcut kod)
        return buildNormalSQL(params)
    }

    fun toList(): List<T> {
        val params = mutableListOf<Any?>()
        val sql = buildSQL(params)

        // Logging
        if (BuildConfig.IS_DEBUG) {
            val startTime = now()

            SqlQueryLogger.logRawQuery(sql, params)

            val results = executeQuery(sql, params)

            val elapsed = (now() - startTime).toInstant().epochSeconds
            SqlQueryLogger.logQueryTime("SELECT", elapsed)
            Logger.d("SQL_RESULT", "Returned ${results.size} rows")

            return results
        }

        return executeQuery(sql, params)
    }
    private fun executeQuery(sql: String, params: List<Any?>): List<T> {
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
        val originalLimit = limitValue
        limit(1)

        val params = mutableListOf<Any?>()
        val sql = buildSQL(params)

        // Logging
        if (BuildConfig.IS_DEBUG) {
            val startTime = now()

            SqlQueryLogger.logRawQuery(sql, params)

            val result = executeSingleQuery(sql, params)

            val elapsed = (now() - startTime).toInstant().epochSeconds
            SqlQueryLogger.logQueryTime("SELECT (firstOrNull)", elapsed)
            Logger.d("SQL_RESULT", "Returned ${if (result != null) "1 row" else "no rows"}")

            // Restore original limit
            limitValue = originalLimit

            return result
        }

        val result = executeSingleQuery(sql, params)

        // Restore original limit
        limitValue = originalLimit

        return result
    }
    private fun executeSingleQuery(sql: String, params: List<Any?>): T? {
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

    /**
     * Kayıt var mı kontrol et (kolonları çekmeden)
     *
     * Örnek:
     * ```
     * val hasActiveUsers = driver.select<UserEntity> {
     *     where { criteria("status", equal = "ACTIVE") }
     * }.any()
     * ```
     *
     * SQL: SELECT 1 FROM UserEntity WHERE status = 'ACTIVE' LIMIT 1
     */
    fun any(): Boolean {
        val params = mutableListOf<Any?>()
        val sql = buildExistsSQL(params)

        if (BuildConfig.IS_DEBUG) {
            val startTime = now()
            SqlQueryLogger.logRawQuery(sql, params)

            val result = executeExistsQuery(sql, params)

            val elapsed = (now() - startTime).toInstant().epochSeconds
            SqlQueryLogger.logQueryTime("EXISTS", elapsed)
            Logger.d("SQL_RESULT", "Exists: $result")

            return result
        }

        return executeExistsQuery(sql, params)
    }

    /**
     * any() ile aynı - alias
     */
    fun exists(): Boolean = any()

    /**
     * Kayıt yok mu kontrol et
     *
     * Örnek:
     * ```
     * val noBlockedUsers = driver.select<UserEntity> {
     *     where { criteria("blocked", equal = true) }
     * }.none()
     * ```
     */
    fun none(): Boolean = !any()

    /**
     * Build lightweight EXISTS query (SELECT 1 ... LIMIT 1)
     */
    private fun buildExistsSQL(params: MutableList<Any?>): String {
        if (baseSql != null) {
            var sql = "SELECT 1 FROM ($baseSql) AS _subquery"

            if (whereCondition != NoCondition) {
                sql += " WHERE ${whereCondition.toSQL(params)}"
            }

            sql += " LIMIT 1"
            return sql
        }

        // Normal build
        var sql = "SELECT 1 FROM ${metadata.tableName}"

        joins.forEach { join ->
            sql += " ${join.toSQL(params)}"
        }

        if (whereCondition != NoCondition) {
            sql += " WHERE ${whereCondition.toSQL(params)}"
        }

        sql += " LIMIT 1"
        return sql
    }

    /**
     * Raw SQL üzerine WHERE/ORDER/GROUP/LIMIT ekle
     */
    private fun buildFromRawSQL(params: MutableList<Any?>): String {
        val sql = StringBuilder(baseSql!!)

        // WHERE
        if (whereCondition != NoCondition) {
            sql.append(" WHERE ")
            sql.append(whereCondition.toSQL(params))
        }

        // GROUP BY
        groupByBuilder?.let { builder ->
            if (builder.groupByFields.isNotEmpty()) {
                sql.append(" GROUP BY ")
                sql.append(builder.groupByFields.joinToString(", "))
            }

            // HAVING
            if (builder.havingCondition != NoCondition) {
                sql.append(" HAVING ")
                sql.append(builder.havingCondition.toSQL(params))
            }
        }

        // ORDER BY
        if (orderSpecs.isNotEmpty()) {
            sql.append(" ORDER BY ")
            sql.append(orderSpecs.joinToString(", ") { "${it.field} ${it.direction.name}" })
        }

        // LIMIT
        limitValue?.let {
            sql.append(" LIMIT ")
            sql.append(it)
        }

        // OFFSET
        offsetValue?.let {
            sql.append(" OFFSET ")
            sql.append(it)
        }

        return sql.toString()
    }

    private fun buildNormalSQL(params: MutableList<Any?>): String {
        val allColumnMappings = mutableMapOf<String, String>()
        joins.forEach { join ->
            allColumnMappings.putAll(join.columnMappings)
        }

        // SELECT kısmı
        val selectColumns = mutableListOf<String>()

        metadata.columns.forEach { column ->
            val columnName = column.name
            if (allColumnMappings.containsKey(columnName)) {
                selectColumns.add("${allColumnMappings[columnName]} AS $columnName")
            } else {
                selectColumns.add("${metadata.tableName}.$columnName")
            }
        }

        joins.forEach { join ->
            if (join.selectedColumns.isNotEmpty()) {
                val alias = join.effectiveTableAlias
                join.selectedColumns.forEach { col ->
                    if (!allColumnMappings.containsKey(col)) {
                        selectColumns.add("$alias.$col")
                    }
                }
            }
        }

        val selectClause = selectColumns.joinToString(", ")

        var sql = "SELECT $selectClause FROM ${metadata.tableName}"

        joins.forEach { join ->
            sql += " ${join.toSQL(params)}"
        }

        if (whereCondition != NoCondition) {
            sql += " WHERE ${whereCondition.toSQL(params)}"
        }

        groupByBuilder?.let { builder ->
            if (builder.groupByFields.isNotEmpty()) {
                sql += " GROUP BY ${builder.groupByFields.joinToString(", ")}"
            }
            if (builder.havingCondition != NoCondition) {
                sql += " HAVING ${builder.havingCondition.toSQL(params)}"
            }
        }

        if (orderSpecs.isNotEmpty()) {
            sql += " ORDER BY " + orderSpecs.joinToString(", ") { "${it.field} ${it.direction.name}" }
        }

        limitValue?.let { sql += " LIMIT $it" }
        offsetValue?.let { sql += " OFFSET $it" }

        return sql
    }

    /**
     * Execute EXISTS query
     */
    private fun executeExistsQuery(sql: String, params: List<Any?>): Boolean {
        var exists = false

        driver.executeQuery(
            identifier = null,
            sql = sql,
            mapper = { cursor ->
                exists = cursor.next().value
                app.cash.sqldelight.db.QueryResult.Value(exists)
            },
            parameters = params.size
        ) {
            params.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        return exists
    }

    // INNER JOIN
    inline fun <reified J : Any> innerJoin(
        leftColumn: String,
        rightColumn: String,
        noinline block: (JoinBuilder.() -> Unit)? = null
    ): SelectBuilder<T> {
        val joinMetadata = EntityMetadataRegistry.get<J>()
        val builder = JoinBuilder(
            joinType = JoinType.INNER,
            tableName = joinMetadata.tableName,
            leftColumn = "${metadata.tableName}.$leftColumn",
            rightColumn = "${joinMetadata.tableName}.$rightColumn"
        )
        block?.invoke(builder)
        joins.add(builder.build())
        return this
    }

    // LEFT JOIN
    inline fun <reified J : Any> leftJoin(
        leftColumn: String,
        rightColumn: String,
        noinline block: (JoinBuilder.() -> Unit)? = null
    ): SelectBuilder<T> {
        val joinMetadata = EntityMetadataRegistry.get<J>()
        val builder = JoinBuilder(
            joinType = JoinType.LEFT,
            tableName = joinMetadata.tableName,
            leftColumn = "${metadata.tableName}.$leftColumn",
            rightColumn = "${joinMetadata.tableName}.$rightColumn"
        )
        block?.invoke(builder)
        joins.add(builder.build())
        return this
    }

    // RIGHT JOIN
    inline fun <reified J : Any> rightJoin(
        leftColumn: String,
        rightColumn: String,
        noinline block: (JoinBuilder.() -> Unit)? = null
    ): SelectBuilder<T> {
        val joinMetadata = EntityMetadataRegistry.get<J>()
        val builder = JoinBuilder(
            joinType = JoinType.RIGHT,
            tableName = joinMetadata.tableName,
            leftColumn = "${metadata.tableName}.$leftColumn",
            rightColumn = "${joinMetadata.tableName}.$rightColumn"
        )
        block?.invoke(builder)
        joins.add(builder.build())
        return this
    }

    // CROSS JOIN
    inline fun <reified J : Any> crossJoin(
        noinline block: (JoinBuilder.() -> Unit)? = null
    ): SelectBuilder<T> {
        val joinMetadata = EntityMetadataRegistry.get<J>()
        val builder = JoinBuilder(
            joinType = JoinType.CROSS,
            tableName = joinMetadata.tableName,
            leftColumn = "",
            rightColumn = ""
        )
        block?.invoke(builder)
        joins.add(builder.build())
        return this
    }

}

// SQLDelight Cursor wrapper
class SqlDelightCursor(
    private val cursor: app.cash.sqldelight.db.SqlCursor
) : Cursor {
    override fun getString(index: Int): String? = cursor.getString(index)
    override fun getLong(index: Int): Long? = cursor.getLong(index)
    override fun getDouble(index: Int): Double? = cursor.getDouble(index)
    override fun getBytes(index: Int): ByteArray? = cursor.getBytes(index)
    override fun getBoolean(index: Int): Boolean? = cursor.getLong(index)?.let { it != 0L }
}