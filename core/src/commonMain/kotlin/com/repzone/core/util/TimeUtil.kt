package com.repzone.core.util

import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.core.util.extensions.now
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Time utilities
 */
@OptIn(ExperimentalTime::class)
object TimeUtils {
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
    fun currentTimeMillis(): Long{
        return now()
    }
    fun getCurrentDayOfWeek(value: Long): DayOfWeek {
        val instant = Instant.fromEpochMilliseconds(value)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.dayOfWeek
    }

    fun getCurrentHour(value: Long): Int {
        val instant = Instant.fromEpochMilliseconds(value)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.hour
    }

    fun getCurrentMinute(value: Long): Int {
        val instant = Instant.fromEpochMilliseconds(value)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.minute
    }

    fun getCurrentSecond(value: Long): Int {
        val instant = Instant.fromEpochMilliseconds(value)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.second
    }

    fun Duration.toTimerFormat(): String {
        return toComponents { hours, minutes, seconds, _ ->
            buildString {
                append(hours.toString().padStart(2, '0'))
                append(':')
                append(minutes.toString().padStart(2, '0'))
                append(':')
                append(seconds.toString().padStart(2, '0'))
            }
        }
    }
}