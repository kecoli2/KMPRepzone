@file:OptIn(ExperimentalTime::class)

package com.repzone.core.util.extensions

import androidx.compose.runtime.Composable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Duration.Companion.days


// ============================================
// Long <-> Instant Conversions
// ============================================

fun Long.toInstant(): Instant =
    Instant.fromEpochMilliseconds(this)

fun Long.toInstantFromSeconds(): Instant =
    Instant.fromEpochSeconds(this)

fun Instant.addDays(days: Int, timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant =
    this.plus(days, DateTimeUnit.DAY, timeZone)


// UTC Long <-> ISO 8601 String Conversions
// ============================================

fun Long.toIso8601String(): String =
    Instant.fromEpochMilliseconds(this).toString()

fun Long.toIso8601StringFromSeconds(): String =
    Instant.fromEpochSeconds(this).toString()

fun String.fromIso8601ToLong(): Long? =
    runCatching { Instant.parse(this).toEpochMilliseconds() }.getOrNull()

fun Long.getCurrentHour(): Int {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.hour
}

fun Long.getCurrentDayOfWeek(): DayOfWeek {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.dayOfWeek
}

fun Long.getCurrentMinute(): Int {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.minute
}

fun Long.getCurrentSecond(): Int {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.second
}

fun Instant.getLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime{
    return this.toLocalDateTime(timeZone)
}

// ============================================
// Date/Time Formatting Extensions
// ============================================

fun Long.toDateOnly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${local.year}-${local.month.number.toString().padStart(2, '0')}-${local.day.toString().padStart(2, '0')}"
}

fun Long.toTimeOnly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}:${local.second.toString().padStart(2, '0')}"
}

fun Long.toTimeOnlyHourMinute(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

fun Long.toShortTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

fun Long.toFullDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    "${toDateOnly(timeZone)} ${toTimeOnly(timeZone)}"

fun Long.toShortDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${local.day.toString().padStart(2, '0')}/${local.month.number.toString().padStart(2, '0')}/${local.year} " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

@Composable
fun Long.toUserFriendly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    val monthName = when (local.month.number) {
        1 -> Res.string.month_january.fromResource();
        2 -> Res.string.month_february.fromResource()
        3 -> Res.string.month_march.fromResource()
        4 -> Res.string.month_april.fromResource()
        5 -> Res.string.month_may.fromResource()
        6 -> Res.string.month_june.fromResource()
        7 -> Res.string.month_july.fromResource()
        8 -> Res.string.month_august.fromResource()
        9 -> Res.string.month_september.fromResource()
        10 -> Res.string.month_october.fromResource()
        11 -> Res.string.month_november.fromResource()
        12 -> Res.string.month_december.fromResource()
        else -> ""
    }
    return "${local.day} $monthName ${local.year}, " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

@Composable
fun Instant.toDayName(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = this.toLocalDateTime(timeZone)
    val dayName = when (local.dayOfWeek) {
        DayOfWeek.MONDAY -> Res.string.weekday1.fromResource()
        DayOfWeek.TUESDAY -> Res.string.weekday2.fromResource()
        DayOfWeek.WEDNESDAY -> Res.string.weekday3.fromResource()
        DayOfWeek.THURSDAY -> Res.string.weekday4.fromResource()
        DayOfWeek.FRIDAY -> Res.string.weekday5.fromResource()
        DayOfWeek.SATURDAY -> Res.string.weekday6.fromResource()
        DayOfWeek.SUNDAY -> Res.string.weekday7.fromResource()
    }
    return "${local.day} $dayName"
}

@Composable
fun Instant?.toDayNameOrEmpty(timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    this?.toDayName(timeZone) ?: ""

fun Long.toDateString(pattern: String, timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val local = instant.toLocalDateTime(timeZone)

    return when (pattern) {
        "ISO" -> instant.toString()

        "yyyy-MM-dd" -> buildString {
            append(local.year); append("-")
            append(local.month.number.toString().padStart(2, '0')); append("-")
            append(local.day.toString().padStart(2, '0'))
        }

        "dd/MM/yyyy" -> buildString {
            append(local.day.toString().padStart(2, '0')); append("/")
            append(local.month.number.toString().padStart(2, '0')); append("/")
            append(local.year)
        }

        "yyyy-MM-dd HH:mm:ss" -> buildString {
            append(local.year); append("-")
            append(local.month.number.toString().padStart(2, '0')); append("-")
            append(local.day.toString().padStart(2, '0')); append(" ")
            append(local.hour.toString().padStart(2, '0')); append(":")
            append(local.minute.toString().padStart(2, '0')); append(":")
            append(local.second.toString().padStart(2, '0'))
        }

        "dd/MM/yyyy HH:mm" -> buildString {
            append(local.day.toString().padStart(2, '0')); append("/")
            append(local.month.number.toString().padStart(2, '0')); append("/")
            append(local.year); append(" ")
            append(local.hour.toString().padStart(2, '0')); append(":")
            append(local.minute.toString().padStart(2, '0'))
        }

        "HH:mm:ss" -> buildString {
            append(local.hour.toString().padStart(2, '0')); append(":")
            append(local.minute.toString().padStart(2, '0')); append(":")
            append(local.second.toString().padStart(2, '0'))
        }

        "HH:mm" -> buildString {
            append(local.hour.toString().padStart(2, '0')); append(":")
            append(local.minute.toString().padStart(2, '0'))
        }

        "yyyy-MM-dd HH:mm:ss.fff" -> buildString {
            append(local.year); append("-")
            append(local.month.number.toString().padStart(2, '0')); append("-")
            append(local.day.toString().padStart(2, '0')); append(" ")
            append(local.hour.toString().padStart(2, '0')); append(":")
            append(local.minute.toString().padStart(2, '0')); append(":")
            append(local.second.toString().padStart(2, '0')); append(".")
            append(local.nanosecond.div(1_000_000).toString().padStart(3, '0'))
        }

        else -> local.toString()
    }
}

// ============================================
// Relative Time (Ago) Extensions
// ============================================

@Composable
fun Long.toRelativeTimeString(): String {
    val now = Clock.System.now()
    val instant = Instant.fromEpochMilliseconds(this)
    val duration = now - instant

    return when {
        duration.inWholeSeconds < 60 -> Res.string.time_just_now.fromResource()
        duration.inWholeMinutes < 60 -> Res.string.time_minutes_ago.fromResource(duration.inWholeMinutes)
        duration.inWholeHours   < 24 -> Res.string.time_hours_ago.fromResource(duration.inWholeHours)
        duration.inWholeDays    < 7  -> Res.string.time_days_ago.fromResource(duration.inWholeDays)
        duration.inWholeDays    < 30 -> Res.string.time_weeks_ago.fromResource(duration.inWholeDays)
        duration.inWholeDays    < 365 -> Res.string.time_months_ago.fromResource(duration.inWholeDays)
        else -> Res.string.time_years_ago.fromResource(duration.inWholeDays / 365)
    }
}

// ============================================
// Date Comparison Extensions
// ============================================

fun Long.isSameDay(
    other: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Boolean {
    val d1 = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
    val d2 = Instant.fromEpochMilliseconds(other).toLocalDateTime(timeZone).date
    return d1 == d2
}

fun Long.isToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean =
    this.isSameDay(Clock.System.now().toEpochMilliseconds(), timeZone)

fun Long.isYesterday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val yesterday = Clock.System.now().minus(1.days).toEpochMilliseconds()
    return this.isSameDay(yesterday, timeZone)
}

fun Long.isPast(): Boolean =
    this < Clock.System.now().toEpochMilliseconds()

fun Long.isFuture(): Boolean =
    this > Clock.System.now().toEpochMilliseconds()

fun Long.isTomorrow(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val tomorrow = Clock.System.now().plus(1.days).toEpochMilliseconds()
    return this.isSameDay(tomorrow, timeZone)
}
// ============================================
// Nullable Long Extensions
// ============================================

fun Long?.toIso8601StringOrNull(): String? = this?.toIso8601String()

fun Long?.toIso8601StringOrDefault(default: String = ""): String =
    this?.toIso8601String() ?: default

@Composable
fun Long?.toUserFriendlyOrEmpty(timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    this?.toUserFriendly(timeZone) ?: ""

// ============================================
// Current Time Helpers
// ============================================

fun now(): Long = Clock.System.now().toEpochMilliseconds()

fun nowInSeconds(): Long = Clock.System.now().epochSeconds
