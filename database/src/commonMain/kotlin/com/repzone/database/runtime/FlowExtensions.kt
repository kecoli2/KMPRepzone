package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// Flow notifier
class DatabaseNotifier {
    private val notifierMap = mutableMapOf<String, MutableStateFlow<Long>>()

    fun notifyChange(tableName: String) {
        val flow = notifierMap.getOrPut(tableName) {
            MutableStateFlow(0L)
        }
        flow.value = flow.value + 1
    }

    fun observeTable(tableName: String): Flow<Long> {
        return notifierMap.getOrPut(tableName) {
            MutableStateFlow(0L)
        }
    }

    fun reset() {
        notifierMap.clear()
    }
}

// Global notifier
val globalNotifier = DatabaseNotifier()

fun SqlDriver.getNotifier(): DatabaseNotifier = globalNotifier

fun SqlDriver.notifyChange(tableName: String) {
    globalNotifier.notifyChange(tableName)
}

// SELECT as Flow
inline fun <reified T : Any> SqlDriver.selectAsFlow(
    noinline block: SelectBuilder<T>.() -> Unit = {}
): Flow<List<T>> {
    val metadata = EntityMetadataRegistry.get<T>()

    return globalNotifier.observeTable(metadata.tableName).map {
        select<T>(block).toList()
    }
}

// COUNT as Flow
inline fun <reified T : Any> SqlDriver.countAsFlow(
    noinline block: (SelectBuilder<T>.() -> Unit)? = null
): Flow<Long> {
    val metadata = EntityMetadataRegistry.get<T>()

    return globalNotifier.observeTable(metadata.tableName).map {
        count<T>(block)
    }
}

// Single entity as Flow
inline fun <reified T : Any> SqlDriver.selectFirstAsFlow(
    noinline block: SelectBuilder<T>.() -> Unit = {}
): Flow<T?> {
    val metadata = EntityMetadataRegistry.get<T>()

    return globalNotifier.observeTable(metadata.tableName).map {
        select<T>(block).firstOrNull()
    }
}

// INSERT with notification
inline fun <reified T : Any> SqlDriver.insertAndNotify(entity: T): Long {
    val metadata = EntityMetadataRegistry.get<T>()
    val id = insert(entity)
    notifyChange(metadata.tableName)
    return id
}

// UPDATE with notification
inline fun <reified T : Any> SqlDriver.updateAndNotify(entity: T): Int {
    val metadata = EntityMetadataRegistry.get<T>()
    val affected = update(entity)
    if (affected > 0) {
        notifyChange(metadata.tableName)
    }
    return affected
}

// DELETE with notification
inline fun <reified T : Any> SqlDriver.deleteAndNotify(entity: T): Int {
    val metadata = EntityMetadataRegistry.get<T>()
    val affected = delete(entity)
    if (affected > 0) {
        notifyChange(metadata.tableName)
    }
    return affected
}

// Batch operations with notification
inline fun <reified T : Any> SqlDriver.batchInsertAndNotify(entities: List<T>): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val metadata = EntityMetadataRegistry.get<T>()
    val ids = batchInsert(entities)
    notifyChange(metadata.tableName)
    return ids
}

inline fun <reified T : Any> SqlDriver.batchUpdateAndNotify(entities: List<T>): Int {
    if (entities.isEmpty()) return 0

    val metadata = EntityMetadataRegistry.get<T>()
    val affected = batchUpdate(entities)
    if (affected > 0) {
        notifyChange(metadata.tableName)
    }
    return affected
}

inline fun <reified T : Any> SqlDriver.batchDeleteAndNotify(entities: List<T>): Int {
    if (entities.isEmpty()) return 0

    val metadata = EntityMetadataRegistry.get<T>()
    val affected = batchDelete(entities)
    if (affected > 0) {
        notifyChange(metadata.tableName)
    }
    return affected
}

// Transaction with notification
fun SqlDriver.transactionWithNotification(
    vararg tableNames: String,
    block: TransactionScope.() -> Unit
) {
    transaction(block)
    tableNames.forEach { table ->
        notifyChange(table)
    }
}