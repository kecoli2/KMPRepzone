package com.repzone.domain.model

data class SyncReservedStockModel(
  val uniqueId: String,
  val organizationId: Long?,
  val productId: Long?,
  val stock: Double?,
  val unitId: Long?,
)
