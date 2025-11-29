package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductParameterDto(
    val id: Int,
    val productId: Int? = null ,
    val order: Int? = null,
    val color: String? = null
)