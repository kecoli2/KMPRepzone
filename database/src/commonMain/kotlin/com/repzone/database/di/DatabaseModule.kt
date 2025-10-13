package com.repzone.database.di

import com.repzone.database.AppDatabase
import com.repzone.database.impl.DatabaseManagerImpl
import com.repzone.database.interfaces.IDatabaseManager
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

val DatabaseModule = module {
    single<IDatabaseManager>{ DatabaseManagerImpl(get(), get()) }

    single {
        runBlocking {
            get<IDatabaseManager>().getDatabase()
        }
    }

    single {
        runBlocking {
            get<IDatabaseManager>().getSqlDriver()
        }
    }

    // Queryler
    single { get<AppDatabase>().syncCustomerEntityQueries }
    single { get<AppDatabase>().syncProductEntityQueries }
    single { get<AppDatabase>().syncModuleEntityQueries }
}