package com.repzone.core.util.extensions

fun List<String>?.joinList(separator: String = ", "): String {
    return this?.joinToString(separator = separator) ?: ""
}