package com.repzone.domain.model

data class SyncProductUnitModel (
    val id: Long,
    val barcode: String?,
    val displayOrder: Int?,
    val isVisible: Boolean,
    val maxOrderQuantity: Int?,
    val minimumOrderQuantity: Int?,
    val modificationDateUtc: Long?,
    val multiplier: Int?,
    val orderQuantityFactor: Int?,
    val price: Double?,
    val priceId: Int?,
    val productId: Long?,
    val purchaseDamagedReturnPrice: Double?,
    val purchasePrice: Double?,
    val purchaseReturnPrice: Double?,
    val recordDateUtc: Long?,
    val salesDamagedReturnPrice: Double?,
    val salesReturnPrice: Double?,
    val selected: Boolean,
    val state: Long?,
    val tenantId: Long?,
    val unitId: Int?,
    val weight: Double?,
)

