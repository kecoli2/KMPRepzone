package com.repzone.domain.model.gps

import com.repzone.core.platform.randomUUID

/**
 * Location Model-Data
 */
data class GpsLocation(
    val id: String = randomUUID(),
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val speed: Float? = null,
    val bearing: Float? = null,
    val altitude: Double? = null,
    val provider: String,
    val isSynced: Boolean = false
)