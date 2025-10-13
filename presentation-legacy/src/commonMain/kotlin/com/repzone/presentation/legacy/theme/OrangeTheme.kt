package com.repzone.presentation.legacy.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.repzone.core.enums.ThemeType
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant

// ==================== TONAL PALETTES ====================

//region Primary Palette (Orange)
private val primary0 = Color(0xFF000000)
private val primary5 = Color(0xFF180A01)
private val primary10 = Color(0xFF301503)
private val primary15 = Color(0xFF4D1E00)
private val primary20 = Color(0xFF662800)
private val primary25 = Color(0xFF803200)
private val primary30 = Color(0xFF993C00)
private val primary35 = Color(0xFFB34600)
private val primary40 = Color(0xFFCC5000)
private val primary50 = Color(0xFFFF6400)
private val primary60 = Color(0xFFFF8333)
private val primary70 = Color(0xFFFFA266)
private val primary80 = Color(0xFFF7C3A1)
private val primary90 = Color(0xFFF7E2D4)
private val primary95 = Color(0xFFF9F1EC)
private val primary99 = Color(0xFFFEFCFB)
private val primary100 = Color(0xFFFFFFFF)
//endregion Primary Palette (Orange)

//region Secondary Palette
private val secondary0 = Color(0xFF000000)
private val secondary5 = Color(0xFF030F16)
private val secondary10 = Color(0xFF071F2C)
private val secondary15 = Color(0xFF072F45)
private val secondary20 = Color(0xFF0A3F5C)
private val secondary25 = Color(0xFF0C4F73)
private val secondary30 = Color(0xFF0F5E8A)
private val secondary35 = Color(0xFF116EA1)
private val secondary40 = Color(0xFF147EB8)
private val secondary50 = Color(0xFF189DE7)
private val secondary60 = Color(0xFF47B1EB)
private val secondary70 = Color(0xFF75C4F0)
private val secondary80 = Color(0xFFA9D6EF)
private val secondary90 = Color(0xFFD7EAF4)
private val secondary95 = Color(0xFFEDF4F7)
private val secondary99 = Color(0xFFFBFDFD)
private val secondary100 = Color(0xFFFFFFFF)
//endregion Secondary Palette

//region Tertiary Palette
private val tertiary0 = Color(0xFF000000)
private val tertiary5 = Color(0xFF130C06)
private val tertiary10 = Color(0xFF26170D)
private val tertiary15 = Color(0xFF3C2211)
private val tertiary20 = Color(0xFF502E16)
private val tertiary25 = Color(0xFF64391C)
private val tertiary30 = Color(0xFF784421)
private val tertiary35 = Color(0xFF8C5027)
private val tertiary40 = Color(0xFFA05B2C)
private val tertiary50 = Color(0xFFC87237)
private val tertiary60 = Color(0xFFD38E5F)
private val tertiary70 = Color(0xFFDEAA87)
private val tertiary80 = Color(0xFFE5C7B3)
private val tertiary90 = Color(0xFFF0E4DB)
private val tertiary95 = Color(0xFFF6F2EF)
private val tertiary99 = Color(0xFFFDFCFC)
private val tertiary100 = Color(0xFFFFFFFF)
//endregion Tertiary Palette

//region Neutral Palette
private val neutral0 = Color(0xFF000000)
private val neutral5 = Color(0xFF0D0D0C)
private val neutral10 = Color(0xFF1A1919)
private val neutral15 = Color(0xFF282625)
private val neutral20 = Color(0xFF353331)
private val neutral25 = Color(0xFF423F3E)
private val neutral30 = Color(0xFF4F4C4A)
private val neutral35 = Color(0xFF5C5956)
private val neutral40 = Color(0xFF696563)
private val neutral50 = Color(0xFF847F7B)
private val neutral60 = Color(0xFF9C9896)
private val neutral70 = Color(0xFFB5B2B0)
private val neutral80 = Color(0xFFCDCCCB)
private val neutral90 = Color(0xFFE6E5E5)
private val neutral95 = Color(0xFFF2F2F2)
private val neutral99 = Color(0xFFFCFCFC)
private val neutral100 = Color(0xFFFFFFFF)
//endregion Neutral Palette

//region Neutral Variant Palette
private val neutralVariant0 = Color(0xFF000000)
private val neutralVariant5 = Color(0xFF0E0D0C)
private val neutralVariant10 = Color(0xFF1C1917)
private val neutralVariant15 = Color(0xFF2A2623)
private val neutralVariant20 = Color(0xFF38332E)
private val neutralVariant25 = Color(0xFF453F3A)
private val neutralVariant30 = Color(0xFF534C46)
private val neutralVariant35 = Color(0xFF615951)
private val neutralVariant40 = Color(0xFF6F655D)
private val neutralVariant50 = Color(0xFF8B7F74)
private val neutralVariant60 = Color(0xFFA29890)
private val neutralVariant70 = Color(0xFFB9B2AC)
private val neutralVariant80 = Color(0xFFD0CCC8)
private val neutralVariant90 = Color(0xFFE7E5E4)
private val neutralVariant95 = Color(0xFFF3F2F2)
private val neutralVariant99 = Color(0xFFFDFCFC)
private val neutralVariant100 = Color(0xFFFFFFFF)
//endregion Neutral Variant Palette

//region Error Palette
private val error0 = Color(0xFF000000)
private val error5 = Color(0xFF2D0001)
private val error10 = Color(0xFF410002)
private val error15 = Color(0xFF5C0004)
private val error20 = Color(0xFF690005)
private val error25 = Color(0xFF7E0007)
private val error30 = Color(0xFF93000A)
private val error35 = Color(0xFFA80710)
private val error40 = Color(0xFFBA1A1A)
private val error50 = Color(0xFFDE3730)
private val error60 = Color(0xFFFF5449)
private val error70 = Color(0xFFFF897D)
private val error80 = Color(0xFFFFB4AB)
private val error90 = Color(0xFFFFDAD6)
private val error95 = Color(0xFFFFEDEA)
private val error99 = Color(0xFFFFFBFF)
private val error100 = Color(0xFFFFFFFF)
//endregion Error Palette

// ==================== LIGHT COLOR SCHEME ====================

private val LightOrangeColorScheme = lightColorScheme(
    primary = primary40,
    onPrimary = primary100,
    primaryContainer = primary90,
    onPrimaryContainer = primary10,

    secondary = secondary40,
    onSecondary = secondary100,
    secondaryContainer = secondary90,
    onSecondaryContainer = secondary10,

    tertiary = tertiary40,
    onTertiary = tertiary100,
    tertiaryContainer = tertiary90,
    onTertiaryContainer = tertiary10,

    error = error40,
    onError = error100,
    errorContainer = error90,
    onErrorContainer = error10,

    background = neutral99,
    onBackground = neutral10,

    surface = neutral99,
    onSurface = neutral10,
    surfaceVariant = neutralVariant90,
    onSurfaceVariant = neutralVariant30,

    surfaceDim = neutral80,
    surfaceBright = neutral99,
    surfaceContainer = neutral95,
    surfaceContainerHigh = neutral90,
    surfaceContainerHighest = neutral90,
    surfaceContainerLow = neutral95,
    surfaceContainerLowest = neutral100,

    outline = neutralVariant50,
    outlineVariant = neutralVariant80,

    inverseSurface = neutral20,
    inverseOnSurface = neutral95,
    inversePrimary = primary80,

    scrim = neutral0,
    surfaceTint = primary40
)


// ==================== DARK COLOR SCHEME ====================
private val DarkOrangeColorScheme = darkColorScheme(
    primary = primary80,
    onPrimary = primary20,
    primaryContainer = primary30,
    onPrimaryContainer = primary90,

    secondary = secondary80,
    onSecondary = secondary20,
    secondaryContainer = secondary30,
    onSecondaryContainer = secondary90,

    tertiary = tertiary80,
    onTertiary = tertiary20,
    tertiaryContainer = tertiary30,
    onTertiaryContainer = tertiary90,

    error = error80,
    onError = error20,
    errorContainer = error30,
    onErrorContainer = error90,

    background = neutral5,
    onBackground = neutral90,

    surface = neutral5,
    onSurface = neutral90,
    surfaceVariant = neutralVariant30,
    onSurfaceVariant = neutralVariant80,

    surfaceDim = neutral5,
    surfaceBright = neutral25,
    surfaceContainer = neutral10,
    surfaceContainerHigh = neutral15,
    surfaceContainerHighest = neutral20,
    surfaceContainerLow = neutral10,
    surfaceContainerLowest = neutral5,

    outline = neutralVariant60,
    outlineVariant = neutralVariant30,

    inverseSurface = neutral90,
    inverseOnSurface = neutral20,
    inversePrimary = primary40,

    scrim = neutral0,
    surfaceTint = primary80
)

fun createOrangeScheme() = ColorSchemeVariant(
    id = ThemeType.DEFAULT,
    name = "Turuncu Tema",
    lightColorScheme = LightOrangeColorScheme,
    darkColorScheme = DarkOrangeColorScheme
)