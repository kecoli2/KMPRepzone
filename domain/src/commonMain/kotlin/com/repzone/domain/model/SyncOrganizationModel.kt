package com.repzone.domain.model

data class SyncOrganizationModel(
  val id: Long,
  val isParent: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val relationId: Long?,
  val state: Long?,
)
