@file:OptIn(ExperimentalTime::class)

package com.repzone.core.util.extensions

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Duration.Companion.days

// ============================================
// UTC Long <-> ISO 8601 String Conversions
// ============================================

// Long (UTC milliseconds) -> ISO 8601 String
fun Long.toIso8601String(): String {
    return Instant.fromEpochMilliseconds(this).toString()
    // Output: "2025-10-01T10:30:45.123Z"
}

// Long (UTC seconds) -> ISO 8601 String
fun Long.toIso8601StringFromSeconds(): String {
    return Instant.fromEpochSeconds(this).toString()
    // Output: "2025-10-01T10:30:45Z"
}

// ISO 8601 String -> Long (milliseconds)
fun String.fromIso8601ToLong(): Long? {
    return try {
        Instant.parse(this).toEpochMilliseconds()
    } catch (e: Exception) {
        null
    }
}

// ============================================
// Date/Time Formatting Extensions
// ============================================

// Long -> Date only (yyyy-MM-dd)
fun Long.toDateOnly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${localDateTime.year}-" +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}-" +
            "${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
}

// Long -> Time only (HH:mm:ss)
fun Long.toTimeOnly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${localDateTime.hour.toString().padStart(2, '0')}:" +
            "${localDateTime.minute.toString().padStart(2, '0')}:" +
            "${localDateTime.second.toString().padStart(2, '0')}"
}

// Long -> Short time (HH:mm)
fun Long.toShortTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${localDateTime.hour.toString().padStart(2, '0')}:" +
            "${localDateTime.minute.toString().padStart(2, '0')}"
}

// Long -> Full DateTime (yyyy-MM-dd HH:mm:ss)
fun Long.toFullDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return "${toDateOnly(timeZone)} ${toTimeOnly(timeZone)}"
}

// Long -> Short DateTime (dd/MM/yyyy HH:mm)
fun Long.toShortDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}/" +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}/" +
            "${localDateTime.year} " +
            "${localDateTime.hour.toString().padStart(2, '0')}:" +
            "${localDateTime.minute.toString().padStart(2, '0')}"
}

// Long -> User Friendly (1 October 2025, 10:30)
fun Long.toUserFriendly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    val monthName = when (localDateTime.monthNumber) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> ""
    }

    return "${localDateTime.dayOfMonth} $monthName ${localDateTime.year}, " +
            "${localDateTime.hour.toString().padStart(2, '0')}:" +
            "${localDateTime.minute.toString().padStart(2, '0')}"
}

// Long -> Custom format
fun Long.toDateString(
    pattern: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(timeZone)

    return when (pattern) {
        "ISO" -> instant.toString() // "2025-10-01T10:30:45.123Z"

        "yyyy-MM-dd" -> buildString {
            append(localDateTime.year)
            append("-")
            append(localDateTime.monthNumber.toString().padStart(2, '0'))
            append("-")
            append(localDateTime.dayOfMonth.toString().padStart(2, '0'))
        }

        "dd/MM/yyyy" -> buildString {
            append(localDateTime.dayOfMonth.toString().padStart(2, '0'))
            append("/")
            append(localDateTime.monthNumber.toString().padStart(2, '0'))
            append("/")
            append(localDateTime.year)
        }

        "yyyy-MM-dd HH:mm:ss" -> buildString {
            append(localDateTime.year)
            append("-")
            append(localDateTime.monthNumber.toString().padStart(2, '0'))
            append("-")
            append(localDateTime.dayOfMonth.toString().padStart(2, '0'))
            append(" ")
            append(localDateTime.hour.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.minute.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.second.toString().padStart(2, '0'))
        }

        "dd/MM/yyyy HH:mm" -> buildString {
            append(localDateTime.dayOfMonth.toString().padStart(2, '0'))
            append("/")
            append(localDateTime.monthNumber.toString().padStart(2, '0'))
            append("/")
            append(localDateTime.year)
            append(" ")
            append(localDateTime.hour.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.minute.toString().padStart(2, '0'))
        }

        "HH:mm:ss" -> buildString {
            append(localDateTime.hour.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.minute.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.second.toString().padStart(2, '0'))
        }

        "HH:mm" -> buildString {
            append(localDateTime.hour.toString().padStart(2, '0'))
            append(":")
            append(localDateTime.minute.toString().padStart(2, '0'))
        }

        else -> localDateTime.toString() // Default format
    }
}

// ============================================
// Relative Time (Ago) Extensions
// ============================================

// Long -> Relative time (2h ago, 3d ago, etc.)
fun Long.toRelativeTimeString(): String {
    val now = Clock.System.now()
    val instant = Instant.fromEpochMilliseconds(this)
    val duration = now - instant

    return when {
        duration.inWholeSeconds < 60 -> "Just now"
        duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes}m ago"
        duration.inWholeHours < 24 -> "${duration.inWholeHours}h ago"
        duration.inWholeDays < 7 -> "${duration.inWholeDays}d ago"
        duration.inWholeDays < 30 -> "${duration.inWholeDays / 7}w ago"
        duration.inWholeDays < 365 -> "${duration.inWholeDays / 30}mo ago"
        else -> "${duration.inWholeDays / 365}y ago"
    }
}

// ============================================
// Date Comparison Extensions
// ============================================

// Check if two Long dates are on the same day
fun Long.isSameDay(
    other: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Boolean {
    val date1 = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
    val date2 = Instant.fromEpochMilliseconds(other).toLocalDateTime(timeZone).date
    return date1 == date2
}

// Check if Long date is today
fun Long.isToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    return this.isSameDay(Clock.System.now().toEpochMilliseconds(), timeZone)
}

// Check if Long date is yesterday
fun Long.isYesterday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val yesterday = Clock.System.now().minus(1.days).toEpochMilliseconds()
    return this.isSameDay(yesterday, timeZone)
}

// Check if Long date is in the past
fun Long.isPast(): Boolean {
    return this < Clock.System.now().toEpochMilliseconds()
}

// Check if Long date is in the future
fun Long.isFuture(): Boolean {
    return this > Clock.System.now().toEpochMilliseconds()
}

// ============================================
// Nullable Long Extensions
// ============================================

// Nullable Long -> ISO 8601 String (returns null if Long is null)
fun Long?.toIso8601StringOrNull(): String? {
    return this?.toIso8601String()
}

// Nullable Long -> ISO 8601 String (returns default if Long is null)
fun Long?.toIso8601StringOrDefault(default: String = ""): String {
    return this?.toIso8601String() ?: default
}

// Nullable Long -> User Friendly (returns empty string if null)
fun Long?.toUserFriendlyOrEmpty(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return this?.toUserFriendly(timeZone) ?: ""
}

// ============================================
// Current Time Helpers
// ============================================

// Get current UTC timestamp in milliseconds
fun now(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

// Get current UTC timestamp in seconds
fun nowInSeconds(): Long {
    return Clock.System.now().epochSeconds
}