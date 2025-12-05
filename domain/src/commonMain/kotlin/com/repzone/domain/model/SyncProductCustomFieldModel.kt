package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductCustomFieldModel(
  val id: Long,
  val entityId: Long?,
  val fieldId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val value: String?,
)
