package com.repzone.presentation.di

import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.presentation.theme.PresentationThemeConfig
import com.repzone.presentation.viewmodel.*
import com.repzone.presentation.viewmodel.login.LoginScreenViewModel
import com.repzone.presentation.viewmodel.sync.SyncTestViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PresentationModule = module {
    single<IPresentationConfig> { PresentationThemeConfig() }
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(named("v1")), get()) }
    factory { SyncTestViewModel(get(), get()) }
}