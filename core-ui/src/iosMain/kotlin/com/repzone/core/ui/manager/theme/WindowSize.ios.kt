@file:OptIn(ExperimentalForeignApi::class)

package com.repzone.core.ui.manager.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIScreen
import kotlinx.cinterop.useContents

@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    return remember {
        val screen = UIScreen.mainScreen
        val bounds = screen.bounds.useContents {
            size.width to size.height
        }

        // iOS point'ten dp'ye yaklaşık dönüşüm
        val widthDp = bounds.first.toInt()
        val heightDp = bounds.second.toInt()

        val widthSizeClass = when {
            widthDp < 600 -> WindowWidthSizeClass.Compact
            widthDp < 840 -> WindowWidthSizeClass.Medium
            else -> WindowWidthSizeClass.Expanded
        }

        val heightSizeClass = when {
            heightDp < 480 -> WindowHeightSizeClass.Compact
            heightDp < 900 -> WindowHeightSizeClass.Medium
            else -> WindowHeightSizeClass.Expanded
        }

        WindowSizeClass(widthSizeClass, heightSizeClass)
    }
}

@Composable
actual fun isLandscape(): Boolean {
    return remember {
        val screen = UIScreen.mainScreen
        val bounds = screen.bounds.useContents {
            size.width to size.height
        }

        // Genişlik yükseklikten büyükse landscape
        bounds.first > bounds.second
    }
}
