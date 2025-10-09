package com.repzone.core.ui.manager.theme.common

interface IPresentationModule {
    val moduleId: String
    val moduleName: String
    fun provideColorSchemes(): List<ColorSchemeVariant>
}