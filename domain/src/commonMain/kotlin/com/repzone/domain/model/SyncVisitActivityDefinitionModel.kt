package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncVisitActivityDefinitionModel(
  val id: Long,
  val customerTags: String?,
  val descriptions: String?,
  val formTags: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val order: Long?,
  val recordDateUtc: Long?,
  val representativeTags: String?,
  val state: StateType,
)
