package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobileProductParameterDto(
    @SerialName("Id")
    val id: Int,

    @SerialName("ProductId")
    val productId: Int,

    @SerialName("Order")
    val order: Int,

    @SerialName("Color")
    val color: String
) {
    companion object {
        fun create(product: MobileProductDto): List<MobileProductParameterDto> {
            return product.parameters.map { model ->
                MobileProductParameterDto(
                    id = model.id,
                    productId = product.id,
                    order = model.order,
                    color = model.color
                )
            }
        }
    }
}