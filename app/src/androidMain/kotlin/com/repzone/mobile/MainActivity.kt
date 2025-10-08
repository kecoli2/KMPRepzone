package com.repzone.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.data.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.firebase.di.FirebaseMockAndroidModule
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.thema.AppTheme
import com.repzone.network.di.NetworkModule
import com.repzone.presentation.di.PresentationModule
import com.repzone.presentation.viewmodel.sync.SyncTestScreen
import com.repzone.sync.di.SyncModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SyncTestScreen()
            }

            //App()
        }
    }
}

@Composable
@Preview
fun AppAndroidPreview() {
    startKoin {
         androidContext(RepzoneApplication()) // Yerine gerçek context kullanın
            modules(
            DatabaseModule,
            DatabaseAndroidModule, AndroidDIModulePreview,
            NetworkModule,
            RepositoryModule,
            SyncModule,
            PresentationModule
        )
    }
    loadKoinModules(FirebaseMockAndroidModule)
    AppTheme { SyncTestScreen() }
}


