package com.repzone.domain.model

data class OrderOutBoxEntity(
    val id: String,
    val payload: String,
    val attemptCount: Long,
    val lastError: String?,
    val nextAttemptAt: Long,
    val createdAt: Long,
)