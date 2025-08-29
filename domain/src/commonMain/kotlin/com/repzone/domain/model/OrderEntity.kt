package com.repzone.domain.model

data class OrderEntity(
    val id: String,
    val customerId: String,
    val total: Long,
    val currency: String,
    val status: String,
    val updatedAt: String
)
