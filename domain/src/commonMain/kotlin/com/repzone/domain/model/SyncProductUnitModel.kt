package com.repzone.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncProductUnitModel (
    @SerialName("Id")
    val id: Long,
    @SerialName("Barcode")
    val barcode: String?,
    @SerialName("DisplayOrder")
    val displayOrder: Int?,
    @SerialName("IsVisible")
    val isVisible: Boolean,
    @SerialName("MaxOrderQuantity")
    val maxOrderQuantity: Int?,
    @SerialName("MinimumOrderQuantity")
    val minimumOrderQuantity: Int?,
    @SerialName("ModificationDateUtc")
    val modificationDateUtc: Long?,
    @SerialName("Multiplier")
    val multiplier: Int?,
    @SerialName("OrderQuantityFactor")
    val orderQuantityFactor: Int?,
    @SerialName("Price")
    val price: Double?,
    @SerialName("PriceId")
    val priceId: Int?,
    @SerialName("ProductId")
    val productId: Long?,
    @SerialName("PurchaseDamagedReturnPrice")
    val purchaseDamagedReturnPrice: Double?,
    @SerialName("PurchasePrice")
    val purchasePrice: Double?,
    @SerialName("PurchaseReturnPrice")
    val purchaseReturnPrice: Double?,
    @SerialName("RecordDateUtc")
    val recordDateUtc: Long?,
    @SerialName("SalesDamagedReturnPrice")
    val salesDamagedReturnPrice: Double?,
    @SerialName("SalesReturnPrice")
    val salesReturnPrice: Double?,
    @SerialName("Selected")
    val selected: Boolean,
    @SerialName("State")
    val state: Long?,
    @SerialName("TenantId")
    val tenantId: Long?,
    @SerialName("UnitId")
    val unitId: Int?,
    @SerialName("Weight")
    val weight: Double?,
)

