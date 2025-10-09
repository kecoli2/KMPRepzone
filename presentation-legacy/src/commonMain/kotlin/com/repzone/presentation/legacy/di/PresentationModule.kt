package com.repzone.presentation.legacy.di

import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.viewmodel.TestScreenViewModel
import com.repzone.presentation.legacy.viewmodel.login.LoginScreenViewModel
import com.repzone.presentation.legacy.viewmodel.sync.SyncTestViewModel
import org.koin.dsl.module

val PresentationModuleLegacy = module {
    single { LegacyThemeConfig() }
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(), get()) }
    factory { SyncTestViewModel(get(), get()) }
}