package com.repzone.database.di

import com.repzone.database.driver.DatabaseDriverFactory
import org.koin.dsl.module

val DatabaseAndroidModule = module {
    single { DatabaseDriverFactory(get()) }
}