package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.Order_outbox
import com.repzone.domain.model.OrderOutBoxEntity

class OrderOutboxDbMapper: Mapper<Order_outbox, OrderOutBoxEntity> {

    override fun toDomain(from: Order_outbox) = OrderOutBoxEntity(
        id = from.id,
        payload = from.payload,
        attemptCount = from.attemptCount,
        lastError = from.lastError,
        nextAttemptAt = from.nextAttemptAt,
        createdAt = from.createdAt
    )

    override fun fromDomain(domain: OrderOutBoxEntity) = Order_outbox(
        id = domain.id,
        payload = domain.payload,
        attemptCount = domain.attemptCount,
        lastError = domain.lastError,
        nextAttemptAt = domain.nextAttemptAt,
        createdAt = domain.createdAt
    )

}