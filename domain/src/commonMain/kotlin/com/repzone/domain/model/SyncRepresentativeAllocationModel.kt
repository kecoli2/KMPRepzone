package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncRepresentativeAllocationModel(
  val id: Long,
  val amount: Double?,
  val begin: Long?,
  val end: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val representativeId: Long?,
  val scope: Long?,
  val state: StateType,
)
