package com.repzone.domain.model

data class SyncMobileWarehouseTotalModel(
    val productId: Long,
    val stock: Double,
    val orderStock: Double
)
