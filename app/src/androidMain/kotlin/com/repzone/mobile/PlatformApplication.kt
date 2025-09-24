package com.repzone.mobile

import android.app.Application
import com.repzone.data.repository.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseAndroidModule
import com.repzone.mobile.di.AndroidLocationModule
import com.repzone.network.di.NetworkModule
import com.repzone.presentation.di.PresentationModule
import com.repzone.sync.di.SyncModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

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
                NetworkModule,
                RepositoryModule,
                SyncModule,
                AndroidLocationModule,
                PresentationModule
            )
        }
        val engine = OkHttp.create()
        val client: HttpClient = getKoin().get { parametersOf(engine, null) }

    /*    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        val job: OrderSyncJob = getKoin().get { parametersOf(client, scope) }
        job.start()*/
        loadKoinModules(FirebaseAndroidModule)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}