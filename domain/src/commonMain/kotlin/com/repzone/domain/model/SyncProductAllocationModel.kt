package com.repzone.domain.model

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
  val state: Long?,
  val unitId: Long?,
)
