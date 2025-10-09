package com.repzone.core.ui.di

import com.repzone.core.ui.manager.theme.ThemeManager
import org.koin.dsl.module

val CoreUiModule = module {
    single { ThemeManager() }
}