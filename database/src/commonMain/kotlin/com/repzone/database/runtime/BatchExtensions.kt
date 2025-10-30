package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

// ============================================
// TRUE BATCH INSERT - Tek SQL statement
// ============================================

/**
 * Gerçek batch insert - Tek SQL statement ile tüm kayıtları ekler
 * INSERT INTO table (col1, col2) VALUES (?, ?), (?, ?), (?, ?)
 */
fun <T : Any> SqlDriver.batchInsert(entities: List<T>): List<Long> {
    if (entities.isEmpty()) return emptyList()

    // İlk entity'den metadata al
    val firstEntity = entities.first()
    val metadata = EntityMetadataRegistry.getMetadata(firstEntity)

    val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }
    val columnNames = insertColumns.joinToString(", ") { it.name }

    // Her entity için (?, ?, ?) oluştur
    val valuesPlaceholder = insertColumns.joinToString(", ") { "?" }
    val allValuesPlaceholders = entities.joinToString(", ") { "($valuesPlaceholder)" }

    // SQL: INSERT INTO table (col1, col2) VALUES (?, ?), (?, ?), (?, ?)
    val sql = "INSERT INTO ${metadata.tableName} ($columnNames) VALUES $allValuesPlaceholders"

    // Tüm entity'lerin value'larını düz liste olarak topla
    val allValues = mutableListOf<Any?>()
    entities.forEach { entity ->
        val values = metadata.extractValues(entity)
        insertColumns.forEach { col ->
            allValues.add(values[col.name])
        }
    }

    // Tek execute ile tüm kayıtları ekle
    execute(
        identifier = null,
        sql = sql,
        parameters = allValues.size
    ) {
        allValues.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    // SQLite'ta RETURNING yok, ID'leri döndüremiyoruz
    // Sadece başarılı olduğunu belirten dummy ID'ler dön
    return List(entities.size) { -1L }
}

/**
 * Batch INSERT OR REPLACE - Tek SQL statement
 */
fun <T : Any> SqlDriver.batchInsertOrReplace(entities: List<T>): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val firstEntity = entities.first()
    val metadata = EntityMetadataRegistry.getMetadata(firstEntity)

    val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }
    val columnNames = insertColumns.joinToString(", ") { it.name }

    val valuesPlaceholder = insertColumns.joinToString(", ") { "?" }
    val allValuesPlaceholders = entities.joinToString(", ") { "($valuesPlaceholder)" }

    val sql = "INSERT OR REPLACE INTO ${metadata.tableName} ($columnNames) VALUES $allValuesPlaceholders"

    val allValues = mutableListOf<Any?>()
    entities.forEach { entity ->
        val values = metadata.extractValues(entity)
        insertColumns.forEach { col ->
            allValues.add(values[col.name])
        }
    }

    execute(
        identifier = null,
        sql = sql,
        parameters = allValues.size
    ) {
        allValues.forEachIndexed { index, value ->
            bindValue(this, index, value)
        }
    }

    return List(entities.size) { -1L }
}

// ============================================
// FALLBACK - Transaction ile tek tek (ID gerekirse)
// ============================================

/**
 * Transaction batch insert - Her entity'yi tek tek ekler ama transaction içinde
 * ID'leri döndürür ama daha yavaş
 */
fun <T : Any> SqlDriver.batchInsertWithIds(entities: List<T>): List<Long> {
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

// ============================================
// BATCH UPDATE - Transaction ile
// ============================================

/**
 * Batch UPDATE - Transaction içinde tek tek
 * SQL'de multi-row UPDATE yok, transaction kullanmalıyız
 */
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

// ============================================
// BATCH DELETE - Transaction ile
// ============================================

/**
 * Batch DELETE - Transaction içinde tek tek
 */
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

/**
 * Batch DELETE with IN clause - Çok daha hızlı!
 * DELETE FROM table WHERE id IN (?, ?, ?)
 */
inline fun <reified T : Any> SqlDriver.batchDeleteByIds(ids: List<Any>): Int {
    if (ids.isEmpty()) return 0

    val metadata = EntityMetadataRegistry.get<T>()
    val pk = metadata.primaryKey

    val placeholders = ids.joinToString(", ") { "?" }
    val sql = "DELETE FROM ${metadata.tableName} WHERE ${pk.name} IN ($placeholders)"

    return execute(
        identifier = null,
        sql = sql,
        parameters = ids.size
    ) {
        ids.forEachIndexed { index, id ->
            bindValue(this, index, id)
        }
    }.value.toInt()
}

// ============================================
// CHUNKED OPERATIONS
// ============================================

/**
 * Chunked batch insert - Büyük listeler için parça parça
 */
fun <T : Any> SqlDriver.batchInsertChunked(
    entities: List<T>,
    chunkSize: Int = 100
): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val allIds = mutableListOf<Long>()

    entities.chunked(chunkSize).forEach { chunk ->
        val ids = batchInsert(chunk)  // Gerçek batch insert
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

// ============================================
// PROGRESS CALLBACKS
// ============================================

fun <T : Any> SqlDriver.batchInsertWithProgress(
    entities: List<T>,
    chunkSize: Int = 100,
    onProgress: (current: Int, total: Int) -> Unit
): List<Long> {
    if (entities.isEmpty()) return emptyList()

    val allIds = mutableListOf<Long>()
    val total = entities.size
    var processed = 0

    entities.chunked(chunkSize).forEach { chunk ->
        val ids = batchInsert(chunk)
        allIds.addAll(ids)
        processed += chunk.size
        onProgress(processed, total)
    }

    return allIds
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