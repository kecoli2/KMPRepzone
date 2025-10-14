package com.repzone.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.core.di.CoreModule
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.di.CoreUiModule
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.data.di.RepositoryModule
import com.repzone.database.di.DatabaseAndroidModule
import com.repzone.database.di.DatabaseModule
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.di.FirebaseMockAndroidModule
import com.repzone.navigation.AppRouter
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.presentation.legacy.di.PresentationModuleLegacy
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.ui.login.LoginScreenLegacy
import com.repzone.presentation.legacy.ui.splash.SplashScreenLegacy
import com.repzone.sync.di.SyncModule
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
            //AppAndroidPreview()
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
@Preview(showBackground = true)
fun AppAndroidPreview() {
    /*val themeManager = ThemeManager()
    val themeConfig: IPresentationConfig = LegacyThemeConfig()*/
    startKoin {
         androidContext(RepzoneApplication()) // Yerine gerçek context kullanın
            modules(
                CoreModule,
            DatabaseModule,
            DatabaseAndroidModule,
            AndroidDIModulePreview,
            PlatformNetworkModule,
            NetworkModule, CoreUiModule,
            RepositoryModule,
            SyncModule,
            PresentationModuleLegacy
        )
    }
    loadKoinModules(FirebaseMockAndroidModule)
    val themeManager : ThemeManager = koinInject()
    themeManager.initialize(LegacyThemeConfig())
    AppTheme(themeManager) {
        /*LoginScreenLegacy {

        }*/

        SplashScreenLegacy()

    }
}


