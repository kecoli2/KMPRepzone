package com.repzone.domain.model

data class SyncDocumentMapProcessStepModel(
  val id: Long,
  val name: String?,
  val objectName: String?,
  val processId: Long?,
  val state: Long?,
  val stepOrder: Long?,
)
