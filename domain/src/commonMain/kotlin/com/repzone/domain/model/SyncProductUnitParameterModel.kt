package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductUnitParameterModel(
  val id: Long,
  val entityId: Long?,
  val entityType: Long?,
  val maxOrderQuantity: Long?,
  val minOrderQuantity: Long?,
  val modificationDateUtc: Long?,
  val productId: Long?,
  val productUnitId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
