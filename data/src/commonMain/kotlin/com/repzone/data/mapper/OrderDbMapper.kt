package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.Orders
import com.repzone.domain.model.OrderEntity

class OrderDbMapper: Mapper<Orders, OrderEntity> {

    override fun toDomain(from: Orders) = OrderEntity(
        id = from.id,
        customerId = from.customerId,
        total = from.total,
        status = from.status,
        currency = from.currency,
        updatedAt = from.updatedAt
    )
    override fun fromDomain(domain: OrderEntity) = Orders(
        id = domain.id,
        customerId = domain.customerId,
        total = domain.total,
        currency = domain.currency,
        status = domain.status,
        updatedAt = domain.updatedAt
    )

}