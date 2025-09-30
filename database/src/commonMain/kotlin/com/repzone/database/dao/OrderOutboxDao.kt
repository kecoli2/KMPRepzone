/*
package com.repzone.database.dao

import com.repzone.database.AppDatabase
import com.repzone.database.Order_outbox

class OrderOutboxDao(private val db: AppDatabase) {
    private val q get() = db.order_outboxQueries

    fun insertOrReplace(row: Order_outbox) = q.insertOrReplace(
        id = row.id,
        payload = row.payload,
        attemptCount = row.attemptCount,
        lastError = row.lastError,
        nextAttemptAt = row.nextAttemptAt,
        createdAt = row.createdAt
    )

    fun selectReady(nowMillis: Long, limit: Long): List<Order_outbox> =
        q.selectReady(nowMillis, limit).executeAsList()

    fun deleteById(id: String) = q.deleteById(id)

    fun updateAttempt(id: String, lastError: String?, nextAttemptAt: Long) =
        q.updateAttempt(lastError, nextAttemptAt, id)
}*/
