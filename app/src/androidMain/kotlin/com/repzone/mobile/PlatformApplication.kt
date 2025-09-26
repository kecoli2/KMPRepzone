package com.repzone.mobile

import android.app.Application
import com.repzone.data.repository.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseAndroidModule
import com.repzone.mobile.di.AndroidDIModule
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.presentation.base.initializeSmartViewModelStore
import com.repzone.presentation.di.PresentationModule
import com.repzone.sync.di.SyncModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules

class PlatformApplication: Application() {

    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlatformApplication)
            modules(
                DatabaseAndroidModule,
                DatabaseModule,
                PlatformNetworkModule,
                NetworkModule,
                RepositoryModule,
                SyncModule,
                AndroidDIModule,
                PresentationModule,
                FirebaseAndroidModule,
            )
        }
        loadKoinModules(FirebaseAndroidModule)
        initializeSmartViewModelStore()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}