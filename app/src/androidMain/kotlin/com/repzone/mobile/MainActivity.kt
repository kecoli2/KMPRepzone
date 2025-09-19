package com.repzone.mobile

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.data.repository.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseMockAndroidModule
import com.repzone.mobile.di.AndroidLocationModule
import com.repzone.mobile.thema.AppTheme
import com.repzone.network.di.NetworkModule
import com.repzone.presentation.di.PresentationModule
import com.repzone.presentation.ui.TestScreen
import com.repzone.sync.di.SyncModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.parametersOf
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.ext.koin.androidContext
import org.koin.compose.getKoin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TestScreen()
            }

            //App()
        }
    }
}

/*
@Composable
@Preview
fun AppAndroidPreview() {
    startKoin {
         androidContext(PlatformApplication()) // Yerine gerçek context kullanın
            modules(
            DatabaseModule,
            DatabaseAndroidModule,
            NetworkModule,
            RepositoryModule,
            SyncModule,
            AndroidLocationModule,
            PresentationModule
        )
    }
    loadKoinModules(FirebaseMockAndroidModule)

    val engine = OkHttp.create()
    val client: HttpClient = getKoin().get { parametersOf(engine, null) }
    AppTheme { TestScreen() }
}

*/

