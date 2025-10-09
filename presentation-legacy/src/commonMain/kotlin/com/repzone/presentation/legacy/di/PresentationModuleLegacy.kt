package com.repzone.presentation.legacy.di

import com.repzone.core.constant.NetWorkModuleConstant
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.viewmodel.TestScreenViewModel
import com.repzone.presentation.legacy.viewmodel.login.LoginScreenViewModel
import com.repzone.presentation.legacy.viewmodel.sync.SyncTestViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PresentationModuleLegacy = module {
    single<IPresentationConfig> { LegacyThemeConfig() }
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(named(NetWorkModuleConstant.V1_VERSION)), get()) }
    factory { SyncTestViewModel(get(), get()) }
}