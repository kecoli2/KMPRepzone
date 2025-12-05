package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncManufacturerParameterModel(
  val id: Long,
  val allowedUnitIds: String?,
  val manufacturerId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
