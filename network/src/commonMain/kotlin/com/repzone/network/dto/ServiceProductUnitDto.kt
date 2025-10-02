package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceProductUnitDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("State")
    val state: Int,
    @SerialName("UnitId")
    val unitId: Int,
    @SerialName("Multiplier")
    val multiplier: Int,
    @SerialName("Barcode")
    val barcode: String? = null,
    @SerialName("Weight")
    val weight: Double? = null,
    @SerialName("DisplayOrder")
    val displayOrder: Int,
    @SerialName("Selected")
    val selected: Boolean,
    @SerialName("PriceId")
    val priceId: Int,
    @SerialName("Price")
    val price: Double,
    @SerialName("MinimumOrderQuantity")
    val minimumOrderQuantity: Int,
    @SerialName("MaxOrderQuantity")
    val maxOrderQuantity: Int,
    @SerialName("OrderQuantityFactor")
    val orderQuantityFactor: Int,
    @SerialName("SalesReturnPrice")
    val salesReturnPrice: Double? = null,
    @SerialName("SalesDamagedReturnPrice")
    val salesDamagedReturnPrice: Double? = null,
    @SerialName("PurchasePrice")
    val purchasePrice: Double? = null,
    @SerialName("PurchaseReturnPrice")
    val purchaseReturnPrice: Double? = null,
    @SerialName("PurchaseDamagedReturnPrice")
    val purchaseDamagedReturnPrice: Double? = null
)
