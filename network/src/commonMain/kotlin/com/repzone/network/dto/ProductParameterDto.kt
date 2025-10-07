package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductParameterDto(
    @SerialName("Id")
    val id: Int,

    @SerialName("ProductId")
    val productId: Int? = null ,

    @SerialName("Order")
    val order: Int? = null,

    @SerialName("Color")
    val color: String? = null
)