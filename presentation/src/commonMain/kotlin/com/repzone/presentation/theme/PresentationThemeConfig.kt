package com.repzone.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.DefaultShapes
import com.repzone.core.ui.manager.theme.DefaultTypography
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant

class PresentationThemeConfig : IPresentationConfig {

    //region Field
    override val moduleId = "presentation"
    override val moduleName = "Modern UI"
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun provideColorSchemes(): List<ColorSchemeVariant> {
        return listOf(
            createOrangeScheme(),
            createRedScheme(),
            createYellowScheme(),
            createBlueScheme()
        )
    }

    override fun getDefaultColorSchemeId(): String = "orange"

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun createOrangeScheme() = ColorSchemeVariant(
        id = "orange",
        name = "Turuncu Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFFFF6B35),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDBD0),
            onPrimaryContainer = Color(0xFF3A0A00),
            secondary = Color(0xFF1A4B7C),
            onSecondary = Color(0xFFFFFFFF),
            background = Color(0xFFFFF8F6),
            onBackground = Color(0xFF201A18),
            surface = Color(0xFFFFF8F6),
            onSurface = Color(0xFF201A18),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFFFB599),
            onPrimary = Color(0xFF5F1600),
            primaryContainer = Color(0xFF852200),
            onPrimaryContainer = Color(0xFFFFDBD0),
            secondary = Color(0xFF9DCAFF),
            onSecondary = Color(0xFF003257),
            background = Color(0xFF181210),
            onBackground = Color(0xFFEDE0DC),
            surface = Color(0xFF181210),
            onSurface = Color(0xFFEDE0DC),
        ),
        typography = DefaultTypography,
        shapes = DefaultShapes
    )
    private fun createRedScheme() = ColorSchemeVariant(
        id = "red",
        name = "Kırmızı Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFFDC143C),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFFFFFBFF),
            onBackground = Color(0xFF201A19),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFFFB4AB),
            onPrimary = Color(0xFF690005),
            background = Color(0xFF201A19),
            onBackground = Color(0xFFEDE0DE),
        )
    )
    private fun createYellowScheme() = ColorSchemeVariant(
        id = "yellow",
        name = "Sarı Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFFFFC107),
            onPrimary = Color(0xFF3E2D00),
            background = Color(0xFFFFFBFF),
            onBackground = Color(0xFF1E1B16),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFEFC84C),
            onPrimary = Color(0xFF3E2D00),
            background = Color(0xFF1E1B16),
            onBackground = Color(0xFFE9E1D9),
        )
    )
    private fun createBlueScheme() = ColorSchemeVariant(
        id = "blue",
        name = "Mavi Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF0061A4),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFFFDFCFF),
            onBackground = Color(0xFF1A1C1E),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFF9ECAFF),
            onPrimary = Color(0xFF003258),
            background = Color(0xFF1A1C1E),
            onBackground = Color(0xFFE2E2E6),
        )
    )
    //endregion
}