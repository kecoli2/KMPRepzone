package com.repzone.domain.model

data class SyncFeedbackCategoryModel(
  val id: Long,
  val description: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
