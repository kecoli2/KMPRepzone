package com.repzone.domain.model

data class SyncManufacturerParameterModel(
  val id: Long,
  val allowedUnitIds: String?,
  val manufacturerId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
