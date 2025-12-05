package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncUnitModel(
  val id: Long,
  val isVisible: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
