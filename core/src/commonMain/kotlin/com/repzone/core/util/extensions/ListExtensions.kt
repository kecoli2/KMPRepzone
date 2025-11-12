package com.repzone.core.util.extensions

fun List<String>?.joinList(separator: String = ", "): String {
    return this?.joinToString(separator = separator) ?: ""
}

inline fun <T> List<T>.moveToFirst(
    predicate: (T) -> Boolean,
    transform: (T) -> T = { it }
): List<T> {
    val index = indexOfFirst(predicate)
    if (index == -1) return this

    val mutable = toMutableList()
    val item = transform(mutable.removeAt(index))
    mutable.add(0, item)
    return mutable
}

fun <T> List<T>.moveToFirst(element: T, transform: (T) -> T = { it }): List<T> {
    if (!this.contains(element)) return this

    val newElement = transform(element)
    return listOf(newElement) + this.filter { it != element }
}