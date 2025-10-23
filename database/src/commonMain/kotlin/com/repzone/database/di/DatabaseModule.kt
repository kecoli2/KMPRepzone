package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.config.BuildConfig
import com.repzone.database.AppDatabase
import com.repzone.database.driver.LoggingDriver
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
            var orginal = get<IDatabaseManager>().getSqlDriver()

            if(BuildConfig.IS_DEBUG){
                LoggingDriver(orginal, "SQLDelight", true)
            }else{
                orginal
            }
        }
    }

    // Queryler
    single { get<AppDatabase>().syncCustomerEntityQueries }
    single { get<AppDatabase>().syncProductEntityQueries }
    single { get<AppDatabase>().syncModuleEntityQueries }
    single { get<AppDatabase>().syncPackageCustomFieldProductEntityQueries }
}

val DatabaseModulePreview = module {
    single { AppDatabase(get<SqlDriver>()) }

    // Queryler
    single { get<AppDatabase>().syncCustomerEntityQueries }
    single { get<AppDatabase>().syncProductEntityQueries }
    single { get<AppDatabase>().syncModuleEntityQueries }
    single { get<AppDatabase>().syncPackageCustomFieldProductEntityQueries }
}