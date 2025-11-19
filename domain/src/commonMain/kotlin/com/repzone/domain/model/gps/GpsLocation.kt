package com.repzone.domain.model.gps

import com.repzone.core.enums.DailyOperationType
import com.repzone.core.platform.randomUUID
import com.repzone.domain.util.format

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
    val isSynced: Boolean = false,
    val altitudeAccuracy: Float? = null,
    val batteryLevel: Int? = null,
    val representativeId: Long? = null,
    var reverseGeocoded: String? = null,
    val organizationId: Int,
    val tenantId: Int,
    var dailyOperationType: DailyOperationType = DailyOperationType.ERROR,
    var description: String? = null
){
    fun isValid(): Boolean {
        return latitude in -90.0..90.0 &&
                longitude in -180.0..180.0 &&
                accuracy > 0
    }

    fun toReadableString(): String {
        return "Lat: ${latitude.format(6)}, Lng: ${longitude.format(6)}, " +
                "Accuracy: ${accuracy.format(1)}m"
    }
}