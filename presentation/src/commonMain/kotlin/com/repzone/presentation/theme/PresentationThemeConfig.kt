package com.repzone.presentation.theme

import com.repzone.core.enums.ThemeType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant

class PresentationThemeConfig(private val userSession: IUserSession? = null) : IPresentationConfig {

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
        return listOf()
    }

    override fun getDefaultColorSchemeId(): ThemeType {
        return userSession?.getActiveSession()?.themeId ?: ThemeType.DEFAULT
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}