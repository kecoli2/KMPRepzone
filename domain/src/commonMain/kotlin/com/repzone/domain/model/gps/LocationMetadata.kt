package com.repzone.domain.model.gps

import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import kotlin.time.Duration

data class LocationMetadata(
    val lastGpsTime: Long?,
    val timeSinceLastGps: Duration,
    val lastLatitude: Double?,
    val lastLongitude: Double?,
    val distanceFromLast: Double?,
    val accuracy: Float?,
    val provider: String?
){
    fun hasLocation(): Boolean = lastLatitude != null && lastLongitude != null

    fun getFormattedTimeSince(): UiText {
        val totalMinutes = timeSinceLastGps.inWholeMinutes
        return when {
            totalMinutes < 1 -> UiText.resource(StringResource.TIME_JUST_NOW)
            totalMinutes < 60 -> UiText.resource(StringResource.TIME_MINUTES_AGO, totalMinutes)
            else -> UiText.dynamic("${totalMinutes / 60} saat ${totalMinutes % 60} dakika önce")
        }
    }
}