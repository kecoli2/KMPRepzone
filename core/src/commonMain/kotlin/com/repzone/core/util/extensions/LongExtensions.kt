package com.repzone.core.util.extensions

/**
 * Enum değerini Long'a çevirir
 */
fun <T : Enum<T>> T.enumToLong(): Long = this.ordinal.toLong()

/**
 * Long değerinden Enum oluşturur
 */
inline fun <reified T : Enum<T>> Long.toEnum(): T? {
    return enumValues<T>().getOrNull(this.toInt())
}