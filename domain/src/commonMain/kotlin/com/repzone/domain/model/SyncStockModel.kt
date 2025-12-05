package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncStockModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val orderStock: Double?,
  val organizationId: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val stock: Double?,
  val unitId: Long?,
)
