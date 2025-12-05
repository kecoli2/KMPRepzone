package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncEventReasonModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val name: String?,
  val reasonType: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tags: String?,
)
