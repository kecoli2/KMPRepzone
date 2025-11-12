package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class VisitInformation(
    val appointmentId: Long,
    val visitId: Long,
    val guid: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val start: Instant? = null,
    val orderIds: String? = null,
    val formIds: String? = null,
    val isItOnRoute: Boolean,
    val isItOnTime: Boolean,
    val isItOnLocation: Boolean,
    val distanceFromCustomerLocation: Int,
    val visitType: Int,
    val selectedCustomerOrganizationId: Int
)