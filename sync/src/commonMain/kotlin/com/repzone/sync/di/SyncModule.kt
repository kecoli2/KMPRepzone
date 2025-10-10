package com.repzone.sync.di

import com.repzone.core.config.BuildConfig
import com.repzone.core.config.UIModule
import com.repzone.sync.factory.newversion.SyncJobFactory
import com.repzone.sync.interfaces.ISyncFactory
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.manager.SyncManagerImpl
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.dsl.module

val SyncModule = module {
    single<ISyncFactory> { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    includes(
        when(BuildConfig.activeUIModule){
            UIModule.NEW -> {
                SyncModuleNew
            }
            UIModule.LEGACY -> {
                SyncModuleLegacy
            }
        }
    )

    //region GENERAL
    single<ISyncManager>{ SyncManagerImpl(get()) }
    //endregion GENERAL
}