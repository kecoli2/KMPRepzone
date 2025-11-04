package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class VisitModel(
  val id: Long,
  val appointmentExceptionId: Long?,
  val appointmentId: Long?,
  val distanceFromCustomerLocation: Long?,
  val finish: Instant?,
  val formIdsRaw: String?,
  val guid: String?,
  val isItOnLocation: Long?,
  val isItOnRoute: Long?,
  val isItOnTime: Long?,
  val latitude: Double?,
  val longitude: Double?,
  val orderIdsRaw: String?,
  val selectedCustomerOrganizationId: Long?,
  val start: Instant?,
  val visitNote: String?,
  val visitType: Long?,
)
