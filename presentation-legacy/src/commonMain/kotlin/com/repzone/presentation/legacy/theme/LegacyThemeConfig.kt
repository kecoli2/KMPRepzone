package com.repzone.presentation.legacy.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant

class LegacyThemeConfig : IPresentationConfig {

    //region Field
    override val moduleId = "presentation_legacy"
    override val moduleName = "Legacy UI"
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun provideColorSchemes(): List<ColorSchemeVariant> {
        return listOf(
            createGreenScheme(),
            createPurpleScheme()
        )
    }

    override fun getDefaultColorSchemeId(): String = "green"
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun createGreenScheme() = ColorSchemeVariant(
        id = "green",
        name = "Yeşil Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF006E1C),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFFFCFDF6),
            onBackground = Color(0xFF1A1C19),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFF79DC76),
            onPrimary = Color(0xFF00390A),
            background = Color(0xFF1A1C19),
            onBackground = Color(0xFFE2E3DC),
        )
    )

    private fun createPurpleScheme() = ColorSchemeVariant(
        id = "purple",
        name = "Mor Tema",
        lightColorScheme = lightColorScheme(
            primary = Color(0xFF6A4FA5),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFFFFFBFE),
            onBackground = Color(0xFF1C1B1F),
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFD0BCFF),
            onPrimary = Color(0xFF3A0073),
            background = Color(0xFF1C1B1F),
            onBackground = Color(0xFFE6E1E5),
        )
    )
    //endregion
}