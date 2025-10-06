package com.repzone.domain.model

data class DynamicListOrganizationModel(
  val id: Long,
  val canUse: Long?,
  val dynamicListId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
