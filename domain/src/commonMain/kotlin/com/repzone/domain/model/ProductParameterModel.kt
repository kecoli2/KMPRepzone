package com.repzone.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductParameterModel(
    @SerialName("Color")
    val color: String?,
    @SerialName("Id")
    val id: Long?,
    @SerialName("IsVisible")
    val isVisible: Boolean?,
    @SerialName("Order")
    val order: Long?,
    @SerialName("OrganizationId")
    val organizationId: Long?,
    @SerialName("ProductId")
    val productId: Long?,
)