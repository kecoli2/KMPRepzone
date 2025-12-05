package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncManufacturerCustomersModel(
  val id: Long,
  val customerId: Long?,
  val fulfillment: Long?,
  val manufacturerId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val salesNotAllowed: Long?,
  val state: StateType,
)
