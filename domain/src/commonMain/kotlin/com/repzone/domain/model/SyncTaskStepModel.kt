package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncTaskStepModel(
  val id: Long,
  val controlType: Long?,
  val controlTypeDescription: String?,
  val modificationDateUtc: Long?,
  val needComplteThisToGoToNextStep: Long?,
  val order: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val taskId: Long?,
  val title: String?,
)
