package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.repzone.database.AppDatabase
import com.repzone.database.driver.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseAndroidModule = module {
    single<SqlDriver> { DatabaseDriverFactory(get()).create() }
}