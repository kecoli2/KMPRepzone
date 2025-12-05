package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductAllocationModel(
  val id: Long,
  val begin: Long?,
  val end: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val productId: Long?,
  val quantity: Long?,
  val recordDateUtc: Long?,
  val scope: Long?,
  val state: StateType,
  val unitId: Long?,
)
