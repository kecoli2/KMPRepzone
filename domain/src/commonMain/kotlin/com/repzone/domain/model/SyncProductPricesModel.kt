package com.repzone.domain.model

data class SyncProductPricesModel(
  val id: Long,
  val begin: Long?,
  val description: String?,
  val end: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
