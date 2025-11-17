package com.repzone.data.util

import com.repzone.core.enums.IStringValueEnum
import com.repzone.core.enums.findEnumByValue
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


// String için
fun List<SyncPackageCustomFieldProductModel>.getStringValue(fieldName: String): String? {
    return find { it.fieldName == fieldName }?.value
}

// String default değerle
fun List<SyncPackageCustomFieldProductModel>.getStringValue(fieldName: String, defaultValue: String): String {
    return getStringValue(fieldName) ?: defaultValue
}

// Boolean için
fun List<SyncPackageCustomFieldProductModel>.getBooleanValue(fieldName: String): Boolean? {
    return find { it.fieldName == fieldName }?.value?.toBooleanStrictOrNull()
}

// Boolean default değerle
fun List<SyncPackageCustomFieldProductModel>.getBooleanValue(fieldName: String, defaultValue: Boolean): Boolean {
    return getBooleanValue(fieldName) ?: defaultValue
}

// Int için
fun List<SyncPackageCustomFieldProductModel>.getIntValue(fieldName: String): Int? {
    return find { it.fieldName == fieldName }?.value?.toIntOrNull()
}

// Int default değerle
fun List<SyncPackageCustomFieldProductModel>.getIntValue(fieldName: String, defaultValue: Int): Int {
    return getIntValue(fieldName) ?: defaultValue
}

fun List<SyncPackageCustomFieldProductModel>.getIntValue(fieldName: String, defaultValue: Int?): Int? {
    return getIntValue(fieldName) ?: defaultValue
}

// Long için
fun List<SyncPackageCustomFieldProductModel>.getLongValue(fieldName: String): Long? {
    return find { it.fieldName == fieldName }?.value?.toLongOrNull()
}

// Long default değerle
fun List<SyncPackageCustomFieldProductModel>.getLongValue(fieldName: String, defaultValue: Long): Long {
    return getLongValue(fieldName) ?: defaultValue
}

// Double için
fun List<SyncPackageCustomFieldProductModel>.getDoubleValue(fieldName: String): Double? {
    return find { it.fieldName == fieldName }?.value?.toDoubleOrNull()
}

// Double default değerle
fun List<SyncPackageCustomFieldProductModel>.getDoubleValue(fieldName: String, defaultValue: Double): Double {
    return getDoubleValue(fieldName) ?: defaultValue
}

// Float için
fun List<SyncPackageCustomFieldProductModel>.getFloatValue(fieldName: String): Float? {
    return find { it.fieldName == fieldName }?.value?.toFloatOrNull()
}

// Float default değerle
fun List<SyncPackageCustomFieldProductModel>.getFloatValue(fieldName: String, defaultValue: Float): Float {
    return getFloatValue(fieldName) ?: defaultValue
}

// Enum için (IStringValueEnum implementasyonu gerekli)
inline fun <reified T> List<SyncPackageCustomFieldProductModel>.getEnumValue(fieldName: String): T? where T : Enum<T>, T : IStringValueEnum {
    val value = find { it.fieldName == fieldName }?.value ?: return null
    return findEnumByValue<T>(value)
}

// Enum default değerle
inline fun <reified T> List<SyncPackageCustomFieldProductModel>.getEnumValue(fieldName: String, defaultValue: T): T where T : Enum<T>, T : IStringValueEnum {
    return getEnumValue<T>(fieldName) ?: defaultValue
}

// DateTime için (ISO 8601 format: 2020-01-01T09:00:00) -> Instant döner
@OptIn(ExperimentalTime::class)
fun List<SyncPackageCustomFieldProductModel>.getDateTimeValue(fieldName: String): Instant? {
    val value = find { it.fieldName == fieldName }?.value ?: return null
    return try {
        val ldt = LocalDateTime.parse(value)
        ldt.toInstant(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
        null
    }
}
