package com.repzone.presentation.legacy.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.repzone.core.enums.ThemeType
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant
import com.repzone.core.ui.manager.theme.common.IColorPalet

// ==================== TONAL PALETTES ====================
private class OrangeTonalPalet public constructor(
): IColorPalet {
    //region Primary Palette (Orange)
    override val primary0 :Color = Color(0xFF000000)
    override val primary5 :Color = Color(0xFF180A01)
    override val primary10 :Color = Color(0xFF301503)
    override val primary15 :Color = Color(0xFF4D1E00)
    override val primary20 :Color = Color(0xFF662800)
    override val primary25 :Color = Color(0xFF803200)
    override val primary30 :Color = Color(0xFF993C00)
    override val primary35 :Color = Color(0xFFB34600)
    override val primary40 :Color = Color(0xFFCC5000)
    override val primary50 :Color = Color(0xFFFF6400)
    override val primary60 :Color = Color(0xFFFF8333)
    override val primary70 :Color = Color(0xFFFFA266)
    override val primary80 :Color = Color(0xFFF7C3A1)
    override val primary90 :Color = Color(0xFFF7E2D4)
    override val primary95 :Color = Color(0xFFF9F1EC)
    override val primary99 :Color = Color(0xFFFEFCFB)
    override val primary100 :Color = Color(0xFFFFFFFF)
    //endregion Primary Palette (Orange)

    //region Secondary Palette
    override val secondary0 :Color = Color(0xFF000000)
    override val secondary5 :Color = Color(0xFF030F16)
    override val secondary10 :Color = Color(0xFF071F2C)
    override val secondary15 :Color = Color(0xFF072F45)
    override val secondary20 :Color = Color(0xFF0A3F5C)
    override val secondary25 :Color = Color(0xFF0C4F73)
    override val secondary30 :Color = Color(0xFF0F5E8A)
    override val secondary35 :Color = Color(0xFF116EA1)
    override val secondary40 :Color = Color(0xFF147EB8)
    override val secondary50 :Color = Color(0xFF189DE7)
    override val secondary60 :Color = Color(0xFF47B1EB)
    override val secondary70 :Color = Color(0xFF75C4F0)
    override val secondary80 :Color = Color(0xFFA9D6EF)
    override val secondary90 :Color = Color(0xFFD7EAF4)
    override val secondary95 :Color = Color(0xFFEDF4F7)
    override val secondary99 :Color = Color(0xFFFBFDFD)
    override val secondary100 :Color = Color(0xFFFFFFFF)
    //endregion Secondary Palette

    //region Tertiary Palette
    override val tertiary0 :Color = Color(0xFF000000)
    override val tertiary5 :Color = Color(0xFF130C06)
    override val tertiary10 :Color = Color(0xFF26170D)
    override val tertiary15 :Color = Color(0xFF3C2211)
    override val tertiary20 :Color = Color(0xFF502E16)
    override val tertiary25 :Color = Color(0xFF64391C)
    override val tertiary30 :Color = Color(0xFF784421)
    override val tertiary35 :Color = Color(0xFF8C5027)
    override val tertiary40 :Color = Color(0xFFA05B2C)
    override val tertiary50 :Color = Color(0xFFC87237)
    override val tertiary60 :Color = Color(0xFFD38E5F)
    override val tertiary70 :Color = Color(0xFFDEAA87)
    override val tertiary80 :Color = Color(0xFFE5C7B3)
    override val tertiary90 :Color = Color(0xFFF0E4DB)
    override val tertiary95 :Color = Color(0xFFF6F2EF)
    override val tertiary99 :Color = Color(0xFFFDFCFC)
    override val tertiary100 :Color = Color(0xFFFFFFFF)
    //endregion Tertiary Palette

    //region Neutral Palette
    override val neutral0: Color = Color(0xFF000000)
    override val neutral5 :Color = Color(0xFF0D0D0C)
    override val neutral10 :Color = Color(0xFF1A1919)
    override val neutral15 :Color = Color(0xFF282625)
    override val neutral20 :Color = Color(0xFF353331)
    override val neutral25 :Color = Color(0xFF423F3E)
    override val neutral30 :Color = Color(0xFF4F4C4A)
    override val neutral35 :Color = Color(0xFF5C5956)
    override val neutral40 :Color = Color(0xFF696563)
    override val neutral50 :Color = Color(0xFF847F7B)
    override val neutral60 :Color = Color(0xFF9C9896)
    override val neutral70 :Color = Color(0xFFB5B2B0)
    override val neutral80 :Color = Color(0xFFCDCCCB)
    override val neutral90 :Color = Color(0xFFE6E5E5)
    override val neutral95 :Color = Color(0xFFF2F2F2)
    override val neutral99 :Color = Color(0xFFFCFCFC)
    override val neutral100 :Color = Color(0xFFFFFFFF)
    //endregion Neutral Palette

    //region Neutral Variant Palette
    override val neutralVariant0 :Color = Color(0xFF000000)
    override val neutralVariant5 :Color = Color(0xFF0E0D0C)
    override  val neutralVariant10 :Color = Color(0xFF1C1917)
    override  val neutralVariant15 :Color = Color(0xFF2A2623)
    override  val neutralVariant20 :Color = Color(0xFF38332E)
    override  val neutralVariant25 :Color = Color(0xFF453F3A)
    override  val neutralVariant30 :Color = Color(0xFF534C46)
    override  val neutralVariant35 :Color = Color(0xFF615951)
    override  val neutralVariant40 :Color = Color(0xFF6F655D)
    override  val neutralVariant50 :Color = Color(0xFF8B7F74)
    override  val neutralVariant60 :Color = Color(0xFFA29890)
    override  val neutralVariant70 :Color = Color(0xFFB9B2AC)
    override  val neutralVariant80 :Color = Color(0xFFD0CCC8)
    override  val neutralVariant90 :Color = Color(0xFFE7E5E4)
    override  val neutralVariant95 :Color = Color(0xFFF3F2F2)
    override  val neutralVariant99 :Color = Color(0xFFFDFCFC)
    override  val neutralVariant100 :Color = Color(0xFFFFFFFF)
    //endregion Neutral Variant Palette

    //region Error Palette
    override  val error0 :Color = Color(0xFF000000)
    override  val error5 :Color = Color(0xFF2D0001)
    override  val error10 :Color = Color(0xFF410002)
    override  val error15 :Color = Color(0xFF5C0004)
    override  val error20 :Color = Color(0xFF690005)
    override  val error25 :Color = Color(0xFF7E0007)
    override  val error30 :Color = Color(0xFF93000A)
    override  val error35 :Color = Color(0xFFA80710)
    override  val error40 :Color = Color(0xFFBA1A1A)
    override  val error50 :Color = Color(0xFFDE3730)
    override  val error60 :Color = Color(0xFFFF5449)
    override  val error70 :Color = Color(0xFFFF897D)
    override  val error80 :Color = Color(0xFFFFB4AB)
    override  val error90 :Color = Color(0xFFFFDAD6)
    override  val error95 :Color = Color(0xFFFFEDEA)
    override  val error99 :Color = Color(0xFFFFFBFF)
    override  val error100 :Color = Color(0xFFFFFFFF)
    //endregion Error Palette

    override val white: Color = Color.White
    override val black: Color = Color.Black

    //region Additional Color
    override val startVisitIconBackGround = Color(0xFF009942)
    override val stopVisitIconBackGround = Color(0xFFBA1A1A)
    //endregion Additional Color

    override fun lightColorScheme(): ColorScheme {
        return lightColorScheme(
        primary = primary50,
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
        surfaceTint = primary40)
    }

    override fun darkColorScheme(): ColorScheme {
        return  darkColorScheme(primary = primary80,
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
            surfaceTint = primary80)

    }
}

fun createOrangeScheme() = ColorSchemeVariant(
    id = ThemeType.DEFAULT,
    name = "Turuncu Tema",
    colorPalet = OrangeTonalPalet()
)