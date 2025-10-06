package com.repzone.domain.model

data class VisitLogInformationModel(
  val id: Long,
  val customerName: String?,
  val recordDate: Long?,
  val restServiceTaskId: Long?,
  val routeAppointmentId: Long?,
  val visitUniqueId: String?,
)
