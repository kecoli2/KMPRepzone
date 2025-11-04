package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class VisitInformation(
    val appointmentId: Long,
    val visitId: Long,
    val guid: String?,
    val longitude: Double?,
    val latitude: Double?,
    val start: Instant?,
    val orderIds: String?,
    val formIds: String?,
    val isItOnRoute: Boolean,
    val isItOnTime: Boolean,
    val isItOnLocation: Boolean,
    val distanceFromCustomerLocation: Int,
    val visitType: Int,
    val selectedCustomerOrganizationId: Int
)