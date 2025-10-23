package com.repzone.domain.util.models

data class EventReasonCode(
    val id: Int,
    val name: String? = null,
    val tags: List<String>? = emptyList()
)
