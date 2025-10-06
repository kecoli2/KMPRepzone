package com.repzone.domain.model

data class SyncUnitModel(
  val id: Long,
  val isVisible: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
