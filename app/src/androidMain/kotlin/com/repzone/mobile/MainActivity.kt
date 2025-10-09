package com.repzone.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.navigation.AppRouter
import com.repzone.presentation.theme.PresentationThemeConfig
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}

@Composable
private fun AppContent() {
    // DI'dan inject et
    val themeManager: ThemeManager = koinInject()
    val themeConfig: IPresentationConfig = koinInject()  // Modern için

    themeManager.initialize(themeConfig)

    AppTheme(themeManager = themeManager) {
        AppRouter()
    }
}

@Composable
@Preview
fun AppAndroidPreview() {
  /*  startKoin {
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
    AppTheme { SyncTestScreen() }*/
}


