package com.repzone.domain.model

data class FormLogInformationModel(
  val id: Long,
  val customerId: Long?,
  val customerName: String?,
  val formId: Long?,
  val isListView: Long?,
  val lastOpenedPage: Long?,
  val recordDate: Long?,
  val restServiceTaskDoneDate: Long?,
  val restServiceTaskId: Long?,
  val restServiceTaskObject: String?,
  val routeAppointmentId: Long?,
  val status: Long?,
  val version: String?,
  val visitUniqueId: String?,
)
