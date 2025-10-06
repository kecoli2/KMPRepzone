package com.repzone.domain.model

data class SyncFormBaseModel(
  val id: Long,
  val data: String?,
  val description: String?,
  val documentTypeId: Long?,
  val formId: Long?,
  val formName: String?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val visibleOption: Long?,
)
