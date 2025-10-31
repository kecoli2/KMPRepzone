package com.repzone.database.di

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase
import com.repzone.database.impl.DatabaseManagerImpl
import com.repzone.database.impl.DatabaseManagerPreview
import com.repzone.database.interfaces.IDatabaseManager
import org.koin.dsl.module

val DatabaseModule = module {
    single<IDatabaseManager>{ DatabaseManagerImpl(get(), get()) }

/*    factory {
        runBlocking {
            get<IDatabaseManager>().getDatabase()
        }
    }

    factory {
        runBlocking {
            var orginal = get<IDatabaseManager>().getSqlDriver()

            if(BuildConfig.IS_DEBUG){
                LoggingDriver(orginal, "SQLDelight", true)
            }else{
                orginal
            }
        }
    }*/
}

val DatabaseModulePreview = module {

    single { AppDatabase(get<SqlDriver>()) }
    single<IDatabaseManager>{ DatabaseManagerPreview(get(), get()) }
    // Queryler
}