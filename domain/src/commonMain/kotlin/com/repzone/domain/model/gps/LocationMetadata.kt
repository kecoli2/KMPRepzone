package com.repzone.domain.model.gps

import kotlin.time.Duration

data class LocationMetadata(
    val lastGpsTime: Long?,
    val timeSinceLastGps: Duration,
    val lastLatitude: Double?,
    val lastLongitude: Double?,
    val distanceFromLast: Double?
)