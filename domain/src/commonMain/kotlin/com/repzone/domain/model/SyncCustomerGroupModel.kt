package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCustomerGroupModel(
  val id: Long,
  val iconIndex: Long?,
  val isDefault: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val shared: Long?,
  val state: StateType,
)
