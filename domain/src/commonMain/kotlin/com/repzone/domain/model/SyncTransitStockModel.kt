package com.repzone.domain.model

data class SyncTransitStockModel(
  val uniqueId: String,
  val organizationId: Long?,
  val productId: Long?,
  val stock: Double?,
  val unitId: Long?,
)
