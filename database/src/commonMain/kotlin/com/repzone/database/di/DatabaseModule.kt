package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase
import org.koin.dsl.module


val DatabaseModule = module {
    single { AppDatabase(get<SqlDriver>()) }
    single { get<AppDatabase>().syncCustomerEntityQueries }
}