package com.repzone.presentation.legacy.di

import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.viewmodel.TestScreenViewModel
import com.repzone.presentation.legacy.viewmodel.login.LoginScreenViewModel
import com.repzone.presentation.legacy.viewmodel.sync.SyncTestViewModel
import org.koin.dsl.module

val PresentationModuleLegacy = module {
    single<IPresentationConfig> { LegacyThemeConfig() }
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(), get(),get<IDatabaseManager>()) }
    factory { SyncTestViewModel(get(), get()) }
}