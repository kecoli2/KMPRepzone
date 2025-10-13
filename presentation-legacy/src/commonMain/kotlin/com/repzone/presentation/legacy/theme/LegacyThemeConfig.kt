package com.repzone.presentation.legacy.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.repzone.core.enums.ThemeType
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
            createOrangeScheme()
        )
    }

    override fun getDefaultColorSchemeId(): ThemeType = ThemeType.DEFAULT
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}