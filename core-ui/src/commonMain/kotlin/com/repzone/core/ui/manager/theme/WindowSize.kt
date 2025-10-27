package com.repzone.core.ui.manager.theme

import androidx.compose.runtime.Composable

enum class WindowWidthSizeClass {
    Compact,    // < 600dp (Telefon)
    Medium,     // 600-840dp (Tablet dikey)
    Expanded    // > 840dp (Tablet yatay)
}

enum class WindowHeightSizeClass {
    Compact,    // < 480dp
    Medium,     // 480-900dp
    Expanded    // > 900dp
}

// Platform bağımsız WindowSizeClass
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
)

@Composable
expect fun rememberWindowSizeClass(): WindowSizeClass

@Composable
expect fun isLandscape(): Boolean