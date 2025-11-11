package com.repzone.core.util.extensions

fun List<String>?.joinList(separator: String = ", "): String {
    return this?.joinToString(separator = separator) ?: ""
}

fun <T> List<T>.moveToFirst(predicate: (T) -> Boolean): List<T> {
    val element = this.firstOrNull(predicate) ?: return this
    return listOf(element) + this.filter { !predicate(it) }
}

fun <T> List<T>.moveToFirst(element: T): List<T> {
    if (!this.contains(element)) return this
    return listOf(element) + this.filter { it != element }
}