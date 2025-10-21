package com.repzone.mobile

import android.content.res.Configuration
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
import com.repzone.data.di.RepositoryModulePreview
import com.repzone.database.di.DatabaseAndroidPreviewModule
import com.repzone.database.di.DatabaseModulePreview
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.di.FirebaseMockAndroidModule
import com.repzone.navigation.AppRouter
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.presentation.legacy.di.PresentationModuleLegacy
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.ui.rotalist.CustomerListScreenLegacy
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
        //AppAndroidPreview()
        AppRouter()
        //CustomerListScreenLegacy(themeManager)
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun AppAndroidPreview() {
    /*val themeManager = ThemeManager()
    val themeConfig: IPresentationConfig = LegacyThemeConfig()*/
    startKoin {
         androidContext(RepzoneApplication()) // Yerine gerçek context kullanın
            modules(
 CoreModule,
            DatabaseModulePreview,
            DatabaseAndroidPreviewModule,
            AndroidDIModulePreview,
            PlatformNetworkModule,
            NetworkModule,
            CoreUiModule,
            RepositoryModulePreview,
            SyncModule,
            PresentationModuleLegacy
        )
    }
    loadKoinModules(FirebaseMockAndroidModule)
    val themeManager : ThemeManager = koinInject()
    themeManager.initialize(LegacyThemeConfig())
    AppTheme(themeManager) {
        CustomerListScreenLegacy()
    }
}

