package com.repzone.core.ui.config

import androidx.compose.runtime.Composable
import com.repzone.core.config.ThemeType
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant

interface IPresentationConfig {
    /**
     * Modül ID'si
     */
    val moduleId: String

    /**
     * Modül adı
     */
    val moduleName: String

    /**
     * Bu modülün tüm renk şemalarını döndür
     */
    fun provideColorSchemes(): List<ColorSchemeVariant>

    /**
     * Default renk şeması ID'si
     */
    fun getDefaultColorSchemeId(): ThemeType
}