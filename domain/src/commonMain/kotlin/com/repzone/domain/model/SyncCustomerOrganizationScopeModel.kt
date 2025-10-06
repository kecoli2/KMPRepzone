package com.repzone.domain.model

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
  val state: Long?,
)
