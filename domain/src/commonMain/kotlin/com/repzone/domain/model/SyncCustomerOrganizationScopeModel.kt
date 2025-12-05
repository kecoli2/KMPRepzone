package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCustomerOrganizationScopeModel(
  val id: Long,
  val customerId: Long?,
  val fulfillment: Long?,
  val manufacturerId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val organizationName: String?,
  val recordDateUtc: Long?,
  val salesNotAllowed: Long?,
  val state: StateType,
)
