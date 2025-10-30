@file:OptIn(ExperimentalTime::class)

package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime

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

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logBatch("INSERT", metadata.tableName, entities.size)
        //SqlQueryLogger.logRawQuery(sql, allValues)

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

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT", elapsed)

        return List(entities.size) { -1L }
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

    // Logging
    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        SqlQueryLogger.logBatch("INSERT OR REPLACE", metadata.tableName, entities.size)
        //SqlQueryLogger.logRawQuery(sql, allValues)

        execute(
            identifier = null,
            sql = sql,
            parameters = allValues.size
        ) {
            allValues.forEachIndexed { index, value ->
                bindValue(this, index, value)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT OR REPLACE", elapsed)

        return List(entities.size) { -1L }
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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("INSERT WITH IDS", metadata.tableName, entities.size)

        transaction {
            entities.forEach { entity ->
                val id = insert(entity)
                insertedIds.add(id)
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT WITH IDS", elapsed)

        return insertedIds
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("UPDATE", metadata.tableName, entities.size)

        transaction {
            entities.forEach { entity ->
                val affected = update(entity)
                totalAffected += affected
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH UPDATE", elapsed)
        Logger.d("SQL_RESULT", "Updated $totalAffected rows")

        return totalAffected
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("DELETE", metadata.tableName, entities.size)

        transaction {
            entities.forEach { entity ->
                val affected = delete(entity)
                totalAffected += affected
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH DELETE", elapsed)
        Logger.d("SQL_RESULT", "Deleted $totalAffected rows")

        return totalAffected
    }

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

    if (BuildConfig.IS_DEBUG) {
        val startTime = now()
        val whereClause = "${pk.name} IN ($placeholders)"
        SqlQueryLogger.logDelete(tableName = metadata.tableName, whereClause = whereClause, parameters = ids)

        val result = execute(
            identifier = null,
            sql = sql,
            parameters = ids.size
        ) {
            ids.forEachIndexed { index, id ->
                bindValue(this, index, id)
            }
        }.value.toInt()

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH DELETE BY IDS", elapsed)
        Logger.d("SQL_RESULT", "Deleted $result rows")

        return result
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("CHUNKED INSERT", metadata.tableName, entities.size)
        Logger.d("SQL_QUERY", "Chunk size: $chunkSize")

        entities.chunked(chunkSize).forEach { chunk ->
            val ids = batchInsert(chunk)  // Gerçek batch insert
            allIds.addAll(ids)
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT CHUNKED", elapsed)

        return allIds
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("CHUNKED UPDATE", metadata.tableName, entities.size)
        Logger.d("SQL_QUERY", "Chunk size: $chunkSize")

        entities.chunked(chunkSize).forEach { chunk ->
            totalAffected += batchUpdate(chunk)
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH UPDATE CHUNKED", elapsed)

        return totalAffected
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("CHUNKED DELETE", metadata.tableName, entities.size)
        Logger.d("SQL_QUERY", "Chunk size: $chunkSize")

        entities.chunked(chunkSize).forEach { chunk ->
            totalAffected += batchDelete(chunk)
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH DELETE CHUNKED", elapsed)

        return totalAffected
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("INSERT WITH PROGRESS", metadata.tableName, entities.size)

        entities.chunked(chunkSize).forEach { chunk ->
            val ids = batchInsert(chunk)
            allIds.addAll(ids)
            processed += chunk.size
            onProgress(processed, total)
            Logger.d("SQL_PROGRESS", "Progress: $processed/$total")
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH INSERT WITH PROGRESS", elapsed)

        return allIds
    }

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

    if (BuildConfig.IS_DEBUG) {
        val metadata = EntityMetadataRegistry.getMetadata(entities.first())
        val startTime = now()
        SqlQueryLogger.logBatch("UPDATE WITH PROGRESS", metadata.tableName, entities.size)

        transaction {
            entities.forEachIndexed { index, entity ->
                val affected = update(entity)
                totalAffected += affected
                onProgress(index + 1, total)
                Logger.d("SQL_PROGRESS", "Progress: ${index + 1}/$total")
            }
        }

        val elapsed = (now() - startTime).toInstant().epochSeconds
        SqlQueryLogger.logQueryTime("BATCH UPDATE WITH PROGRESS", elapsed)

        return totalAffected
    }

    transaction {
        entities.forEachIndexed { index, entity ->
            val affected = update(entity)
            totalAffected += affected
            onProgress(index + 1, total)
        }
    }

    return totalAffected
}