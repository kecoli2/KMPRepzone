package com.repzone.domain.model.gps

import com.repzone.domain.util.format

data class TrackingStatistics(
    val totalLocations: Int,
    val totalDistanceMeters: Double,
    val averageAccuracy: Float,
    val timeSinceLastGps: kotlin.time.Duration,
    val unsyncedCount: Int
) {
    val totalDistanceKm: Double get() = totalDistanceMeters / 1000.0

    fun getFormattedDistance(): String {
        return when {
            totalDistanceMeters < 1000 -> "${totalDistanceMeters.format(0)} m"
            else -> "${totalDistanceKm.format(2)} km"
        }
    }
}