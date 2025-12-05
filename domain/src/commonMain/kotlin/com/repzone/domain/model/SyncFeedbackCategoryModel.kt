package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncFeedbackCategoryModel(
  val id: Long,
  val description: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
