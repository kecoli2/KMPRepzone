package com.repzone.domain.model

data class TaskLogInformationModel(
  val id: Long,
  val customerId: Long?,
  val date: Long?,
  val fileUrl: String?,
  val formDataUniqueId: String?,
  val formId: Long?,
  val latitude: Double?,
  val longitude: Double?,
  val note: String?,
  val status: Long?,
  val stepId: Long?,
  val taskId: Long?,
  val uniqueId: String?,
  val visitId: Long?,
  val visitUniqueId: String?,
)
