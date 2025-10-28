package com.repzone.mobile

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.repzone.core.di.CoreModule
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.di.CoreUiModule
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
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
import com.repzone.presentation.legacy.ui.customerlist.CustomerListScreenLegacy
import com.repzone.sync.di.SyncModule
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val themeManager: ThemeManager by inject()
    private val presentationConfig: IPresentationConfig by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        themeManager.initialize(presentationConfig)
        lifecycleScope.launch {
            themeManager.languageChangeEvent.collect { languageCode ->
                updateAppLocaleAndRecreate(languageCode)
            }
        }

        setContent {
            AppContent()
        }
    }
    private fun updateAppLocaleAndRecreate(languageCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        themeManager.onDestroy()
    }
}

@Composable
private fun AppContent() {
    val themeManager: ThemeManager = koinInject()
    val currentLanguage by themeManager.currentLanguage.collectAsState()
    androidx.compose.runtime.key(currentLanguage) {
        AppTheme(themeManager = themeManager) {
            AppRouter()
        }
    }
}

@Composable
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 800, heightDp = 400)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun AppAndroidPreview() {
    startKoin {
        androidContext(RepzoneApplication())
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
    val themeManager: ThemeManager = koinInject()
    themeManager.initialize(LegacyThemeConfig())

    var showFilterSheet by remember { mutableStateOf(true) }
    var selectedGroups by remember { mutableStateOf<List<String>>(emptyList()) }

    AppTheme(themeManager) {
        CustomerListScreenLegacy {
        }
        //SettingsScreen()
    }
}