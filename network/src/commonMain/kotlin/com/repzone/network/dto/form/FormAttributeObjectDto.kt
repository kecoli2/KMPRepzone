package com.repzone.network.dto.form

import kotlinx.serialization.Serializable

@Serializable
data class FormAttributeObjectDto(
    val key: String? = null,
    val value: String? = null,
    val alias: String? = null,
    val description: String? = null,
    val valueType: String? = null
)
