package com.repzone.domain.model

data class SyncRepresentativeCustomFieldModel(
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
