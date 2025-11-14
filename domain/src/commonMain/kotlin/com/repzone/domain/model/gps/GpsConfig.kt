package com.repzone.domain.model.gps

/**
 * Gps Configuration sonrasında calisma anında degisebilir default
 */
data class GpsConfig(
    val gpsIntervalMinutes: Int = 5,
    val serverSyncIntervalMinutes: Int = 15,
    val minDistanceMeters: Float = 10f,
    val accuracyThreshold: Float = 50f,
    val batteryOptimizationEnabled: Boolean = true
)