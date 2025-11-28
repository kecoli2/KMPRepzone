package com.repzone.database.runtime

/**
 * JOIN Operations Support
 *
 * Desteklenen JOIN tipleri:
 * - INNER JOIN
 * - LEFT JOIN
 * - RIGHT JOIN
 * - CROSS JOIN
 *
 * Örnek kullanım:
 *
 * // INNER JOIN
 * driver.select<Order> {
 *     innerJoin<Customer>("customerId", "id")
 *     where {
 *         criteria("Customer.status", equal = "ACTIVE")
 *     }
 * }.toList()
 */

// ============================================
// 1. JOIN Types
// ============================================

enum class JoinType(val sql: String) {
    INNER("INNER JOIN"),
    LEFT("LEFT JOIN"),
    RIGHT("RIGHT JOIN"),
    CROSS("CROSS JOIN")
}

// ============================================
// 2. JOIN Configuration
// ============================================

/**
 * JOIN yapılandırması
 */
data class JoinConfig(
    val joinType: JoinType,
    val tableName: String,
    val tableAliasName: String? = null,
    val leftColumn: String,
    val rightColumn: String,
    val selectedColumns: List<String> = emptyList(),
    val additionalConditions: Condition = NoCondition,
    // Entity kolonlarını JOIN kolonlarıyla değiştir
    val columnMappings: Map<String, String> = emptyMap()  // entityColumn -> "alias.joinColumn"
) {
    val effectiveTableAlias: String
        get() = tableAliasName ?: tableName

    fun toSQL(params: MutableList<Any?>): String {
        val alias = effectiveTableAlias
        val table = if (tableAliasName != null) "$tableName AS $tableAliasName" else tableName

        // rightColumn'da alias kullan
        val rightColumnWithAlias = if (tableAliasName != null) {
            // Eğer rightColumn "SyncCustomerEntity.Id" formatındaysa
            // "c.Id" yap
            rightColumn.replace(tableName, alias)
        } else {
            rightColumn
        }

        var sql = if (joinType == JoinType.CROSS) {
            "${joinType.sql} $table"
        } else {
            "${joinType.sql} $table ON $leftColumn = $rightColumnWithAlias"
        }

        // Additional conditions
        if (additionalConditions != NoCondition) {
            val additionalSQL = additionalConditions.toSQL(params)
            if (additionalSQL.isNotEmpty()) {
                sql += " AND $additionalSQL"
            }
        }

        return sql
    }
}

// ============================================
// 3. JOIN Builder
// ============================================

class JoinBuilder(
    private val joinType: JoinType,
    private val tableName: String,
    private val leftColumn: String,
    private val rightColumn: String
) {
    private var tableAlias: String? = null
    private val selectedColumns = mutableListOf<String>()
    private var additionalConditions: Condition = NoCondition
    private val columnMappings = mutableMapOf<String, String>()

    /**
     * Tablo için alias belirt
     */
    fun alias(alias: String): JoinBuilder {
        tableAlias = alias
        return this
    }

    /**
     * Seçilecek kolonlar (entity'de olmayan ekstra kolonlar)
     */
    fun columns(vararg cols: String): JoinBuilder {
        selectedColumns.addAll(cols)
        return this
    }

    /**
     * Entity kolonunu JOIN'den gelen aynı isimli kolonla değiştir
     *
    */
    fun mapColumn(entityColumn: String): JoinBuilder {
        val alias = tableAlias ?: tableName
        columnMappings[entityColumn] = "$alias.$entityColumn"
        return this
    }

    /**
     * Entity kolonunu JOIN'den gelen farklı isimli kolonla değiştir
     *
     */
    fun mapColumn(entityColumn: String, joinColumn: String): JoinBuilder {
        val alias = tableAlias ?: tableName
        columnMappings[entityColumn] = "$alias.$joinColumn"
        return this
    }

    /**
     * Birden fazla kolonu aynı anda map et
     *
     */
    fun mapColumns(vararg columns: String): JoinBuilder {
        columns.forEach { mapColumn(it) }
        return this
    }

    /**
     * Ek JOIN koşulları
     */
    fun on(block: CriteriaBuilder.() -> Unit): JoinBuilder {
        val builder = CriteriaBuilder()
        builder.block()
        additionalConditions = builder.build()
        return this
    }

    /**
     * Config oluştur
     */
    fun build(): JoinConfig {
        return JoinConfig(
            joinType = joinType,
            tableName = tableName,
            tableAliasName = tableAlias,
            leftColumn = leftColumn,
            rightColumn = rightColumn,
            selectedColumns = selectedColumns,
            additionalConditions = additionalConditions,
            columnMappings = columnMappings
        )
    }
}



/**
 * JOIN sonucu için wrapper class
 */
data class JoinResult<T : Any, J : Any>(
    val main: T,
    val joined: J?
)

/**
 * Multiple JOIN için
 */
data class JoinResultMultiple<T : Any>(
    val main: T,
    val joined: Map<String, Any?>
)