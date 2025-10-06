package com.repzone.domain.model

data class TaskVisitLogInformationModel(
  val id: Long,
  val customerId: Long?,
  val finishDate: Long?,
  val startDate: Long?,
  val taskId: Long?,
  val visitUniqueId: String?,
)
