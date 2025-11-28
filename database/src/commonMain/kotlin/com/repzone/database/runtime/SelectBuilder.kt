package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SelectBuilder<T>(public val metadata: EntityMetadata, private val driver: SqlDriver) {
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

        // Tüm JOIN'lerden column mapping'leri topla
        val allColumnMappings = mutableMapOf<String, String>()
        joins.forEach { join ->
            allColumnMappings.putAll(join.columnMappings)
        }

        // SELECT kısmı - JOIN varsa joined kolonları da ekle
        val selectColumns = mutableListOf<String>()

        // Ana tablo kolonları (mapping varsa JOIN'den al)
        metadata.columns.forEach { column ->
            val columnName = column.name
            if (allColumnMappings.containsKey(columnName)) {
                // Bu kolon JOIN'den gelecek
                selectColumns.add("${allColumnMappings[columnName]} AS $columnName")
            } else {
                // Entity kolonunu kullan
                selectColumns.add("${metadata.tableName}.$columnName")
            }
        }

        // JOIN'lerden ekstra kolonlar (columns() ile belirtilmiş, mapping'de olmayanlar)
        joins.forEach { join ->
            if (join.selectedColumns.isNotEmpty()) {
                val alias = join.effectiveTableAlias
                join.selectedColumns.forEach { col ->
                    // Eğer bu kolon mapping'de yoksa ekle
                    if (!allColumnMappings.containsKey(col)) {
                        selectColumns.add("$alias.$col")
                    }
                }
            }
        }

        val selectClause = selectColumns.joinToString(", ")

        // FROM kısmı
        var sql = "SELECT $selectClause FROM ${metadata.tableName}"

        // JOIN'leri ekle
        joins.forEach { join ->
            sql += " ${join.toSQL(params)}"
        }

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

        // Build OFFSET clause
        val offsetClause = offsetValue?.let { " OFFSET $it" } ?: ""

        // Final SQL
        return sql +
                whereClause +
                groupByClause +
                havingClause +
                orderByClause +
                limitClause +
                offsetClause
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
        // SELECT 1 - kolonları çekme
        var sql = "SELECT 1 FROM ${metadata.tableName}"

        // JOIN'leri ekle
        joins.forEach { join ->
            sql += " ${join.toSQL(params)}"
        }

        // WHERE clause
        if (whereCondition != NoCondition) {
            sql += " WHERE ${whereCondition.toSQL(params)}"
        }

        // LIMIT 1 - sadece bir kayıt yeterli
        sql += " LIMIT 1"

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