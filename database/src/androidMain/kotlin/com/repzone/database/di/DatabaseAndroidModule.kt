package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.driver.DatabaseDriverFactory
import org.koin.dsl.module

val DatabaseAndroidModule = module {
    single { DatabaseDriverFactory(get()) }
}

val DatabaseAndroidPreviewModule = module {
    single<SqlDriver> { DatabaseDriverFactory(get()).createDriver(1) }
}