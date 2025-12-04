package com.repzone.core.util.extensions

import androidx.compose.ui.graphics.Color

fun String.toColorOrNull(): Color? {
    return try {
        toColor()
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun String.toColorOrDefault(default: Color = Color.Transparent): Color {
    if (isEmpty()) return default
    return toColorOrNull() ?: default
}

fun String.toColor(): Color {
    val colorString = this.trim().removePrefix("#")

    return when (colorString.length) {
        // RGB format: "RGB" -> "FFRRGGBB"
        3 -> {
            val r = colorString[0].toString().repeat(2)
            val g = colorString[1].toString().repeat(2)
            val b = colorString[2].toString().repeat(2)
            Color(("FF$r$g$b").toLong(16))
        }
        // ARGB format: "ARGB" -> "AARRGGBB"
        4 -> {
            val a = colorString[0].toString().repeat(2)
            val r = colorString[1].toString().repeat(2)
            val g = colorString[2].toString().repeat(2)
            val b = colorString[3].toString().repeat(2)
            Color(("$a$r$g$b").toLong(16))
        }
        // RRGGBB format
        6 -> {
            Color(("FF$colorString").toLong(16))
        }
        // AARRGGBB format
        8 -> {
            Color(colorString.toLong(16))
        }
        else -> throw IllegalArgumentException("Geçersiz renk formatı: $this")
    }
}