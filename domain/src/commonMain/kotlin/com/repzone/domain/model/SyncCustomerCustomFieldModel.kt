package com.repzone.domain.model

data class SyncCustomerCustomFieldModel(
  val id: Long,
  val entityId: Long?,
  val fieldId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val value: String?,
)
