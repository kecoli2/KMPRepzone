package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class DynamicListOrganizationModel(
  val id: Long,
  val canUse: Long?,
  val dynamicListId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
