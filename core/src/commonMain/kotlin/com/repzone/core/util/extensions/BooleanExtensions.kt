package com.repzone.core.util.extensions

fun Long.toBoolean(): Boolean = this != 0L

fun Long?.toBooleanSafe(): Boolean = this != null && this != 0L

fun Long?.toBoolean(nullAsFalse: Boolean = true): Boolean {
    return when {
        this == null -> nullAsFalse
        this == 0L -> false
        else -> true
    }
}

fun Long?.toBoolean(): Boolean = this != null && this != 0L

// Boolean -> Long
fun Boolean.toLong(): Long = if (this) 1L else 0L