package com.repzone.database.runtime

interface EntityMetadata {
    val tableName: String
    val columns: List<ColumnMetadata>
    val primaryKey: ColumnMetadata

    fun createInstance(cursor: Cursor, offset: Int = 0): Any
    fun extractValues(entity: Any): Map<String, Any?>
}

data class ColumnMetadata(
    val name: String,
    val type: ColumnType,
    val isNullable: Boolean,
    val isPrimaryKey: Boolean,
    val isAutoIncrement: Boolean = false,
    val defaultValue: Any? = null,
    val foreignKey: ForeignKeyConstraint? = null
)

data class ForeignKeyConstraint(
    val referencedTable: String,
    val referencedColumn: String,
    val onDelete: ForeignKeyAction = ForeignKeyAction.NO_ACTION,
    val onUpdate: ForeignKeyAction = ForeignKeyAction.NO_ACTION
)

enum class ForeignKeyAction {
    NO_ACTION,
    RESTRICT,
    SET_NULL,
    SET_DEFAULT,
    CASCADE
}

enum class ColumnType {
    INTEGER,
    REAL,
    TEXT,
    BLOB
}

// Cursor interface - SQLDelight'ın cursor'ını wrap edeceğiz
interface Cursor {
    fun getString(index: Int): String?
    fun getLong(index: Int): Long?
    fun getDouble(index: Int): Double?
    fun getBytes(index: Int): ByteArray?
    fun getBoolean(index: Int): Boolean?
}