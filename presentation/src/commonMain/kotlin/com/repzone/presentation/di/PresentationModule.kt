package com.repzone.presentation.di

import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.presentation.theme.PresentationThemeConfig
import org.koin.dsl.module

val PresentationModule = module {
    single<IPresentationConfig> { PresentationThemeConfig(get()) }

}