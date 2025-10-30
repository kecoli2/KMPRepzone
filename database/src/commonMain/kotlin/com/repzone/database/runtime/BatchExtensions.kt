package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

// Batch INSERT
fun <T : Any> SqlDriver.batchInsert(entities: List<T>): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val insertedIds = mutableListOf<Long>()

    transaction {
        entities.forEach { entity ->
            val id = insert(entity)
            insertedIds.add(id)
        }
    }

    return insertedIds
}

// Batch UPDATE
fun <T : Any> SqlDriver.batchUpdate(entities: List<T>): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0

    transaction {
        entities.forEach { entity ->
            val affected = update(entity)
            totalAffected += affected
        }
    }

    return totalAffected
}

// Batch DELETE
fun <T : Any> SqlDriver.batchDelete(entities: List<T>): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0

    transaction {
        entities.forEach { entity ->
            val affected = delete(entity)
            totalAffected += affected
        }
    }

    return totalAffected
}

// Batch DELETE with criteria - daha performanslı
inline fun <reified T : Any> SqlDriver.batchDeleteWhere(
    noinline block: DeleteBuilder<T>.() -> Unit
): Int {
    return delete<T>(block)
}

// Batch UPSERT (INSERT or UPDATE)
fun <T : Any> SqlDriver.batchUpsert(
    entities: List<T>,
    checkExists: (T) -> Boolean
): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0

    transaction {
        entities.forEach { entity ->
            if (checkExists(entity)) {
                totalAffected += update(entity)
            } else {
                insert(entity)
                totalAffected++
            }
        }
    }

    return totalAffected
}

// Chunked batch operations - büyük listeler için
fun <T : Any> SqlDriver.batchInsertChunked(
    entities: List<T>,
    chunkSize: Int = 100
): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val allIds = mutableListOf<Long>()

    entities.chunked(chunkSize).forEach { chunk ->
        val ids = batchInsert(chunk)
        allIds.addAll(ids)
    }

    return allIds
}

fun <T : Any> SqlDriver.batchUpdateChunked(
    entities: List<T>,
    chunkSize: Int = 100
): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0

    entities.chunked(chunkSize).forEach { chunk ->
        totalAffected += batchUpdate(chunk)
    }

    return totalAffected
}

fun <T : Any> SqlDriver.batchDeleteChunked(
    entities: List<T>,
    chunkSize: Int = 100
): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0

    entities.chunked(chunkSize).forEach { chunk ->
        totalAffected += batchDelete(chunk)
    }

    return totalAffected
}

// Batch operations with progress callback
fun <T : Any> SqlDriver.batchInsertWithProgress(
    entities: List<T>,
    onProgress: (current: Int, total: Int) -> Unit
): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val insertedIds = mutableListOf<Long>()
    val total = entities.size

    transaction {
        entities.forEachIndexed { index, entity ->
            val id = insert(entity)
            insertedIds.add(id)
            onProgress(index + 1, total)
        }
    }

    return insertedIds
}

fun <T : Any> SqlDriver.batchUpdateWithProgress(
    entities: List<T>,
    onProgress: (current: Int, total: Int) -> Unit
): Int {
    if (entities.isEmpty()) return 0

    var totalAffected = 0
    val total = entities.size

    transaction {
        entities.forEachIndexed { index, entity ->
            val affected = update(entity)
            totalAffected += affected
            onProgress(index + 1, total)
        }
    }

    return totalAffected
}