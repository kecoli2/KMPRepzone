package com.repzone.domain.util

import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.core.util.extensions.now
import com.repzone.domain.model.gps.GpsLocation
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Distance Calculator
 * Haversine formülü ile iki GPS koordinatı arası mesafe hesaplama
 */
object DistanceCalculator {
    private const val EARTH_RADIUS_METERS = 6371000.0 // Dünya yarıçapı (metre)

    /**
     * İki GPS noktası arası mesafeyi hesaplar (metre)
     * Haversine formula kullanır
     */
    fun calculateDistance(from: GpsLocation, to: GpsLocation): Double {
        return calculateDistance(
            from.latitude, from.longitude,
            to.latitude, to.longitude
        )
    }

    /**
     * İki koordinat arası mesafeyi hesaplar (metre)
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()

        val a = sin(dLat / 2).pow(2) +
                cos(lat1.toRadians()) * cos(lat2.toRadians()) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_METERS * c
    }

    /**
     * Bearing (yön açısı) hesaplar
     * @return 0-360 arası derece
     */
    fun calculateBearing(from: GpsLocation, to: GpsLocation): Double {
        val lat1 = (from.latitude).toRadians()
        val lat2 = (to.latitude).toRadians()
        val dLon = (to.longitude - from.longitude).toRadians()

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        var bearing = (atan2(y, x)).toDegrees()
        bearing = (bearing + 360) % 360

        return bearing
    }

    /**
     * Speed hesaplar (m/s)
     */
    fun calculateSpeed(from: GpsLocation, to: GpsLocation): Double {
        val distance = calculateDistance(from, to)
        val timeDiffSeconds = (to.timestamp - from.timestamp) / 1000.0

        return if (timeDiffSeconds > 0) distance / timeDiffSeconds else 0.0
    }
}


/**
 * Time utilities
 */
object TimeUtils {
    fun currentTimeMillis(): Long = now()

    fun formatDuration(durationMillis: Long): UiText {
        val seconds = durationMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> UiText.resource(StringResource.TIME_DAYS_AGO, days)
            hours > 0 -> UiText.resource(StringResource.TIME_HOURS_AGO, hours)
            minutes > 0 -> UiText.resource(StringResource.TIME_MINUTES_AGO, minutes)
            else -> UiText.resource(StringResource.TIME_JUST_NOW)
        }
    }
}

/**
 * Extension functions
 */

// GpsLocation extensions
fun GpsLocation.distanceTo(other: GpsLocation): Double {
    return DistanceCalculator.calculateDistance(this, other)
}

fun GpsLocation.bearingTo(other: GpsLocation): Double {
    return DistanceCalculator.calculateBearing(this, other)
}

fun GpsLocation.speedTo(other: GpsLocation): Double {
    return DistanceCalculator.calculateSpeed(this, other)
}

fun GpsLocation.isAccurate(threshold: Float = 50f): Boolean {
    return accuracy <= threshold
}

fun GpsLocation.ageInMillis(): Long {
    return TimeUtils.currentTimeMillis() - timestamp
}

fun GpsLocation.isRecent(maxAgeMillis: Long = 60_000): Boolean {
    return ageInMillis() < maxAgeMillis
}

// Double extensions for coordinates
fun Double.toRadians(): Double = this * PI / 180
fun Double.toDegrees(): Double = this * 180 / PI

fun Double.isValidLatitude(): Boolean = this in -90.0..90.0
fun Double.isValidLongitude(): Boolean = this in -180.0..180.0

// Format coordinates
fun Double.formatCoordinate(isLatitude: Boolean): String {
    val direction = when {
        isLatitude -> if (this >= 0) "N" else "S"
        else -> if (this >= 0) "E" else "W"
    }
    val rounded = round(abs(this) * 1000000) / 1000000.0
    return "$rounded° $direction"
}

/**
 * Validation utilities
 */
object ValidationUtils {
    fun isValidGpsLocation(latitude: Double, longitude: Double): Boolean {
        return latitude.isValidLatitude() && longitude.isValidLongitude()
    }

    fun isValidAccuracy(accuracy: Float): Boolean {
        return accuracy > 0 && accuracy < 10000 // Max 10km accuracy
    }

    fun isValidSpeed(speed: Float?): Boolean {
        return speed == null || (speed >= 0 && speed < 500) // Max 500 m/s (~1800 km/h)
    }
}

/**
 * Retry utility
 */
suspend fun <T> retryWithBackoff(
    times: Int = 3,
    initialDelayMillis: Long = 1000,
    maxDelayMillis: Long = 10000,
    factor: Double = 2.0,
    block: suspend () -> T
): Result<T> {
    var currentDelay = initialDelayMillis
    repeat(times - 1) { attempt ->
        try {
            return Result.success(block())
        } catch (e: Exception) {
            kotlinx.coroutines.delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
    }
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
