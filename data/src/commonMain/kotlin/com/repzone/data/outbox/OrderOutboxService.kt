package com.repzone.data.outbox

/*
import com.repzone.core.util.json
import com.repzone.data.mapper.OrderDbMapper
import com.repzone.data.mapper.OrderOutboxDbMapper
import com.repzone.database.Order_outbox
import com.repzone.database.dao.OrderOutboxDao
import com.repzone.domain.model.OrderEntity
import com.repzone.domain.model.OrderOutBoxEntity
import io.ktor.util.date.getTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OrderOutboxService(
    private val orderOutBoxDao: OrderOutboxDao,
    private val outboxMapper: OrderOutboxDbMapper,
    private val orderDtoMapper: OrderDbMapper,
    private val clock: () -> Long = { getTimeMillis() } // inject edilebilir
) {
    fun enqueue(order: OrderEntity) {
        val payload = json.encodeToString(orderDtoMapper.fromDomain(order))
        val now = clock()
        orderOutBoxDao.insertOrReplace(
            Order_outbox(
                id = order.id,
                payload = payload,
                attemptCount = 0,
                lastError = null,
                nextAttemptAt = now,
                createdAt = now
            )
        )
    }

    fun takeBatch(limit: Int): List<OrderOutBoxEntity> =
        orderOutBoxDao.selectReady(clock(), limit.toLong()).map(outboxMapper::toDomain)

    fun success(id: String) {
        orderOutBoxDao.deleteById(id)
    }

    fun failWithBackoff(id: String, attempt: Int, error: String?) {
        val next = nextBackoff(attempt)
        orderOutBoxDao.updateAttempt(id, error, clock() + next.inWholeMilliseconds)
    }

    private fun nextBackoff(attempt: Int): Duration {
        // exponential backoff with jitter: base 2^n seconds between 1..60
        val base = (1 shl attempt).coerceAtMost(60)
        val jitter = (0..base).random()
        return (base + jitter).seconds
    }
}*/
