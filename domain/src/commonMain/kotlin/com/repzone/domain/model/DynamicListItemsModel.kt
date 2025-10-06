package com.repzone.domain.model

data class DynamicListItemsModel(
  val id: Long,
  val dynamicListId: Long?,
  val entityId: Long?,
  val itemType: Long?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
