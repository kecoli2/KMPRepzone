package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncManufacturersModel(
  val id: Long,
  val customerId: Long?,
  val docNote: String?,
  val fulfillment: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
