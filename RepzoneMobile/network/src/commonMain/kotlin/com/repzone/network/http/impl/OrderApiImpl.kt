package com.repzone.network.http.impl

import com.repzone.network.api.IOrderApi
import com.repzone.network.dto.OrderDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class OrderApiImpl(private val client: HttpClient) : IOrderApi {

    override suspend fun push(order: OrderDto): Result<Unit> = runCatching {
        client.post("/orders") { setBody(order) }.body<Unit>()
    }
}