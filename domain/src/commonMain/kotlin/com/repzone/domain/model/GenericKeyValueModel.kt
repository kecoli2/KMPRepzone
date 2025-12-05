package com.repzone.domain.model

data class GenericKeyValueModel(
    val key: Int,
    val name: String,
    val description: String? = null,
    val isDefault: Boolean = false
)
