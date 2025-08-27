package com.repzone.domain.repository

import com.repzone.domain.model.OrderEntity

interface IOrderRepository : CrudRepository<String, OrderEntity> {
    suspend fun pending(): List<OrderEntity>
}