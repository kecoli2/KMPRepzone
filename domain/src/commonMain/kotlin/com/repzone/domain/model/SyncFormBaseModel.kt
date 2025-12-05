package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncFormBaseModel(
  val id: Long,
  val data: String?,
  val description: String?,
  val documentTypeId: Long?,
  val formId: Long?,
  val formName: String?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val visibleOption: Long?,
)
