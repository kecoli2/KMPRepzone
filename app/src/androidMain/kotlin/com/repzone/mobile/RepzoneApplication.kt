package com.repzone.mobile

import android.app.Application
import com.repzone.core.config.BuildConfig
import com.repzone.core.config.UIModule
import com.repzone.data.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseAndroidModule
import com.repzone.mobile.di.AndroidDIModule
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.sync.di.SyncModule
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class RepzoneApplication: Application() {

    //region Field
    private val transactionCoordinator: TransactionCoordinator by inject()
    private val baseAppModule = module {
        includes(
            DatabaseAndroidModule,
            DatabaseModule,
            PlatformNetworkModule,
            NetworkModule,
            RepositoryModule,
            SyncModule,
            AndroidDIModule,
            FirebaseAndroidModule,
        )
    }
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RepzoneApplication)
            modules(
                baseAppModule,
                getActiveUIModule()
            )
        }
        loadKoinModules(FirebaseAndroidModule)
        this.initializeSmartViewModelStore()
    }
    //endregion

    //region Protected Method

    override fun onTerminate() {
        super.onTerminate()
        transactionCoordinator.shutdown()
    }
    //endregion

    //region Private Method
    private fun getActiveUIModule(): Module {
        return when (BuildConfig.activeUIModule) {
            UIModule.NEW -> {
                com.repzone.presentation.di.PresentationModule
            }

            UIModule.LEGACY -> {
                com.repzone.presentationlegacy.di.PresentationModuleLegacy
            }
        }
    }

    private fun initializeSmartViewModelStore() {
        when (BuildConfig.activeUIModule) {
            UIModule.NEW -> {
                com.repzone.presentation.base.initializeSmartViewModelStore()
            }

            UIModule.LEGACY -> {
                com.repzone.presentationlegacy.base.initializeSmartViewModelStore()
            }
        }
        //endregion
    }
}