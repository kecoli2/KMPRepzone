package com.repzone.core.ui.manager.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.repzone.core.enums.ThemeMode

@Composable
fun AppTheme(themeManager: ThemeManager , content: @Composable () -> Unit) {
    val themeMode by themeManager.themeMode.collectAsState()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme
    }

    // Aktif renk şemasını al
    val colorSchemeVariant = themeManager.getCurrentColorScheme()!!

    val colorScheme =  if (isDarkTheme) {
            colorSchemeVariant.colorPalet.darkColorScheme()
        } else {
            colorSchemeVariant.colorPalet.lightColorScheme()
        }


    val typography = (colorSchemeVariant.typography as? Typography) ?: DefaultTypography
    val shapes = (colorSchemeVariant.shapes as? Shapes) ?: DefaultShapes

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography as androidx.compose.material3.Typography,
        shapes = shapes,
        content = content
    )
}