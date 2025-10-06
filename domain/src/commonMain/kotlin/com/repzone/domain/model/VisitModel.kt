package com.repzone.domain.model

data class VisitModel(
  val id: Long,
  val appointmentExceptionId: Long?,
  val appointmentId: Long?,
  val distanceFromCustomerLocation: Long?,
  val finish: Long?,
  val formIdsRaw: String?,
  val guid: String?,
  val isItOnLocation: Long?,
  val isItOnRoute: Long?,
  val isItOnTime: Long?,
  val latitude: Double?,
  val longitude: Double?,
  val orderIdsRaw: String?,
  val selectedCustomerOrganizationId: Long?,
  val start: Long?,
  val visitNote: String?,
  val visitType: Long?,
)
