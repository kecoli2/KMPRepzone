package com.repzone.domain.model

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
  val state: Long?,
)
