package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.repzone.database.AppDatabase
import org.koin.dsl.module

val DatabaseIosModule = module {
    single<SqlDriver> { NativeSqliteDriver(schema = AppDatabase.Schema, name = "app.db") }
}