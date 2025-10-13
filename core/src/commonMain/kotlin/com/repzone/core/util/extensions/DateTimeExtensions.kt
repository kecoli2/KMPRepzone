@file:OptIn(ExperimentalTime::class)

package com.repzone.core.util.extensions

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Duration.Companion.days

// UTC Long <-> ISO 8601 String Conversions
// ============================================

fun Long.toIso8601String(): String =
    Instant.fromEpochMilliseconds(this).toString()

fun Long.toIso8601StringFromSeconds(): String =
    Instant.fromEpochSeconds(this).toString()

fun String.fromIso8601ToLong(): Long? =
    runCatching { Instant.parse(this).toEpochMilliseconds() }.getOrNull()

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

fun Long.toUserFriendly(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    val monthName = when (local.month.number) {
        1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
        5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
        9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
        else -> ""
    }
    return "${local.day} $monthName ${local.year}, " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

fun Long.toDateString(
    pattern: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
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

        else -> local.toString()
    }
}

// ============================================
// Relative Time (Ago) Extensions
// ============================================

fun Long.toRelativeTimeString(): String {
    val now = Clock.System.now()
    val instant = Instant.fromEpochMilliseconds(this)
    val duration = now - instant

    return when {
        duration.inWholeSeconds < 60 -> "Just now"
        duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes}m ago"
        duration.inWholeHours   < 24 -> "${duration.inWholeHours}h ago"
        duration.inWholeDays    < 7  -> "${duration.inWholeDays}d ago"
        duration.inWholeDays    < 30 -> "${duration.inWholeDays / 7}w ago"
        duration.inWholeDays    < 365 -> "${duration.inWholeDays / 30}mo ago"
        else -> "${duration.inWholeDays / 365}y ago"
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

// ============================================
// Nullable Long Extensions
// ============================================

fun Long?.toIso8601StringOrNull(): String? = this?.toIso8601String()

fun Long?.toIso8601StringOrDefault(default: String = ""): String =
    this?.toIso8601String() ?: default

fun Long?.toUserFriendlyOrEmpty(timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    this?.toUserFriendly(timeZone) ?: ""

// ============================================
// Current Time Helpers
// ============================================

fun now(): Long = Clock.System.now().toEpochMilliseconds()

fun nowInSeconds(): Long = Clock.System.now().epochSeconds
