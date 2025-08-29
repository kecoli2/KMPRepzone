package com.repzone.network.api

import com.repzone.network.dto.OrderDto

interface IOrderApi {
    suspend fun push(order: OrderDto): Result<Unit>
    // suspend fun fetchUpdatedSince(sinceIso: String): Result<List<OrderDto>>
}