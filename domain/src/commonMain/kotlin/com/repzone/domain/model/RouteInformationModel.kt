package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class RouteInformationModel(
  val appointmentId: Long,
  val start: Instant?,
  val end: Instant?,
  val customerId: Long?,
  val description: String?,
  val customerName: String?,
  val photoUrl: String?,
  val code: String?,
  val customerGroupName: String?,
  val customerOrganizationId: Long?,
  val customerTagStr: String?,
  val customerRisk: Double?,
  val customerBalance: Double?,
  val isECustomer: Long?,
  val blocked: Long?,
  val customerGroupId: Long?,
  val addressId: Long?,
  val latitude: Double?,
  val longitude: Double?,
  val street: String?,
  val city: String?,
  val district: String?,
)
