package com.repzone.presentation.di

import com.repzone.presentation.viewmodel.TestScreenViewModel
import org.koin.dsl.module

val PresentationModule = module {
    factory { TestScreenViewModel(get(), get()) }   // her ekranda yeni instance
}