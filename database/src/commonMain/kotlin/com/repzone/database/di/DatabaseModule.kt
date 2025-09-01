package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase
import org.koin.dsl.module


val DatabaseModule = module {
    single { AppDatabase(get<SqlDriver>()) }

    //DAO lar
    single { com.repzone.database.dao.OrderDao(get()) }
    single { com.repzone.database.dao.OrderOutboxDao(get()) }
}