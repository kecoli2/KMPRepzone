package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobileProductParameterDto(
    @SerialName("Id")
    val id: Int,

    @SerialName("ProductId")
    val productId: Int? = null ,

    @SerialName("Order")
    val order: Int? = null,

    @SerialName("Color")
    val color: String? = null
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