package com.repzone.core.util.extensions

inline fun <reified T : Enum<T>> Int.toEnum(): T? {
    return enumValues<T>().getOrNull(this)
}

fun Int.toMilisecond(): Long {
    return this * 60 * 1000L
}