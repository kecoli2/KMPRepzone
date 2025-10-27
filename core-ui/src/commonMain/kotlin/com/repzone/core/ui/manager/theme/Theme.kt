package com.repzone.core.ui.manager.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.repzone.core.enums.ThemeMode

// YENİ: ThemeManager için CompositionLocal
val LocalThemeManager = staticCompositionLocalOf<ThemeManager> {
    error("No ThemeManager provided")
}

@Composable
fun AppTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val themeMode by themeManager.themeMode.collectAsState()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme
    }

    // YENİ: Window size'ı algıla ve ThemeManager'a bildir
    val windowSizeClass = rememberWindowSizeClass()
    val isLandscapeMode = isLandscape()

    // Her değişiklikte ThemeManager'ı güncelle
    LaunchedEffect(windowSizeClass, isLandscapeMode) {
        themeManager.updateResponsiveState(
            windowSizeClass = windowSizeClass,
            isLandscape = isLandscapeMode
        )
    }

    // Aktif renk şemasını al
    val colorSchemeVariant = themeManager.getCurrentColorScheme()

    val colorScheme = if (isDarkTheme) {
        colorSchemeVariant.colorPalet.darkColorScheme()
    } else {
        colorSchemeVariant.colorPalet.lightColorScheme()
    }

    val typography = (colorSchemeVariant.typography as? Typography) ?: DefaultTypography
    val shapes = (colorSchemeVariant.shapes as? Shapes) ?: DefaultShapes

    CompositionLocalProvider(LocalThemeManager provides themeManager) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography as androidx.compose.material3.Typography,
            shapes = shapes,
            content = content
        )
    }
}

// YENİ: Kolay erişim için helper object
object AppTheme {
    // Dimensions (responsive)
    val dimens: Dimensions
        @Composable
        get() = LocalThemeManager.current.responsiveState.value.dimensions

    // Window size
    val windowSize: WindowSizeClass
        @Composable
        get() = LocalThemeManager.current.responsiveState.value.windowSizeClass

    // Landscape kontrolü
    val isLandscape: Boolean
        @Composable
        get() = LocalThemeManager.current.responsiveState.value.isLandscape

    // Dark mode kontrolü
    val isDarkMode: Boolean
        @Composable
        get() {
            val themeMode = LocalThemeManager.current.themeMode.value
            val isSystemDark = isSystemInDarkTheme()
            return when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemDark
                else -> { false }
            }
        }

    // Mevcut dil
    val currentLanguage: String
        @Composable
        get() = LocalThemeManager.current.currentLanguage.value

    // ThemeManager'a direkt erişim
    val manager: ThemeManager
        @Composable
        get() = LocalThemeManager.current
}