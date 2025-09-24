package com.repzone.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.data.repository.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseMockAndroidModule
import com.repzone.mobile.thema.AppTheme
import com.repzone.network.di.NetworkModule
import com.repzone.network.http.TokenProvider
import com.repzone.presentation.di.PresentationModule
import com.repzone.presentation.ui.TestScreen
import com.repzone.presentation.ui.login.LoginScreen
import com.repzone.sync.di.SyncModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.compose.getKoin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                LoginScreen()
            }

            //App()
        }
    }
}

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
            PresentationModule
        )
    }
    loadKoinModules(FirebaseMockAndroidModule)

    val engine = OkHttp.create()
    val tokenProvider: TokenProvider? = null // Preview için null
    val client: HttpClient = getKoin().get { parametersOf(engine, tokenProvider) }
    AppTheme { LoginScreen() }
}


