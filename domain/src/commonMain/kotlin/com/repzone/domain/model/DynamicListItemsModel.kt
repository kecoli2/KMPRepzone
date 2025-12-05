package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class DynamicListItemsModel(
  val id: Long,
  val dynamicListId: Long?,
  val entityId: Long?,
  val itemType: Long?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
