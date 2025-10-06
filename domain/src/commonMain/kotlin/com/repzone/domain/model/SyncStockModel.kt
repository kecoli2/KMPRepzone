package com.repzone.domain.model

data class SyncStockModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val orderStock: Double?,
  val organizationId: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val stock: Double?,
  val unitId: Long?,
)
