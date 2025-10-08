package com.repzone.presentationlegacy.di

import com.repzone.presentationlegacy.viewmodel.TestScreenViewModel
import com.repzone.presentationlegacy.viewmodel.login.LoginScreenViewModel
import com.repzone.presentationlegacy.viewmodel.sync.SyncTestViewModel
import org.koin.dsl.module

val PresentationModuleLegacy = module {
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(), get()) }
    factory { SyncTestViewModel(get(), get()) }
}