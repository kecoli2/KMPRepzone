package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncDocumentMapProcessStepModel(
  val id: Long,
  val name: String?,
  val objectName: String?,
  val processId: Long?,
  val state: StateType,
  val stepOrder: Long?,
)
