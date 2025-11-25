package com.repzone.domain.document.model

/**
 * Müşteri
 */
data class Customer(
    val id: String,
    val name: String,
    val code: String,
    val groupId: String?,
    val extraGroupIds: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)
