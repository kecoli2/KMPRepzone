package com.repzone.presentation.di

import com.repzone.presentation.viewmodel.*
import com.repzone.presentation.viewmodel.login.LoginScreenViewModel
import org.koin.dsl.module

val PresentationModule = module {
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
    factory { LoginScreenViewModel(get(), get()) }
}