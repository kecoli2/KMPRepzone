package com.repzone.domain.model

data class SyncProductPriceLinesModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val price: Double?,
  val priceListId: Long?,
  val productId: Long?,
  val productUnitId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val vat: Double?,
)
