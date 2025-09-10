package com.repzone.mobile.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


// ---- Light palette (istediğiniz hex'leri girin) ----
private val LightPrimary           = Color(0xFF2B6CB0)
private val LightOnPrimary         = Color(0xFFFFFFFF)
private val LightPrimaryContainer  = Color(0xFFD0E4FF)
private val LightOnPrimaryContainer= Color(0xFF001C37)

private val LightSecondary         = Color(0xFF6B7280)
private val LightTertiary          = Color(0xFF10B981)

private val LightBackground        = Color(0xFFF7F7FB)
private val LightOnBackground      = Color(0xFF111827)
private val LightSurface           = Color(0xFFFFFFFF)
private val LightOnSurface         = Color(0xFF111827)
private val LightSurfaceVariant    = Color(0xFFE6E8ED)
private val LightOnSurfaceVariant  = Color(0xFF414753)
private val LightOutline           = Color(0xFFB8BDC7)
private val LightError             = Color(0xFFB00020)


val AppLightColors: ColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    error = LightError
)

// ---- Dark palette ----
private val DarkPrimary           = Color(0xFF93C5FD)
private val DarkOnPrimary         = Color(0xFF002B5A)
private val DarkPrimaryContainer  = Color(0xFF134074)
private val DarkOnPrimaryContainer= Color(0xFFD0E4FF)

private val DarkSecondary         = Color(0xFFA3AAB7)
private val DarkTertiary          = Color(0xFF34D399)

private val DarkBackground        = Color(0xFF0B1220)
private val DarkOnBackground      = Color(0xFFE5E7EB)
private val DarkSurface           = Color(0xFF101826)
private val DarkOnSurface         = Color(0xFFE5E7EB)
private val DarkSurfaceVariant    = Color(0xFF2A3140)
private val DarkOnSurfaceVariant  = Color(0xFFC7CCD6)
private val DarkOutline           = Color(0xFF4B5563)
private val DarkError             = Color(0xFFFFB4AB)

val AppDarkColors: ColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    error = DarkError
)