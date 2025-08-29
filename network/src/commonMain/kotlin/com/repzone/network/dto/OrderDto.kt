package com.repzone.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    val customerId: String,
    val total: Long,
    val currency: String,
    val status: String,
    val updatedAtIso: String
)