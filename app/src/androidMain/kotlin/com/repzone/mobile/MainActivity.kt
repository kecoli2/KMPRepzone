@file:OptIn(ExperimentalTime::class)

package com.repzone.mobile

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.repzone.core.di.CoreModule
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.di.CoreUiModule
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.data.di.RepositoryModulePreview
import com.repzone.database.di.DatabaseAndroidPreviewModule
import com.repzone.database.di.DatabaseModulePreview
import com.repzone.domain.platform.providerImpl.AndroidGpsEnabler
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.di.FirebaseMockAndroidModule
import com.repzone.navigation.AppRouter
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.platform.FirebaseManager
import com.repzone.presentation.legacy.di.PresentationModuleLegacy
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.ui.visit.VisitActionList
import com.repzone.preview.ActivityVisit_Sample
import com.repzone.preview.DiscountScreen_Sample
import com.repzone.preview.ProductListScreen_Sample
import com.repzone.preview.Productrow_Preview
import com.repzone.sync.di.SyncModule
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import java.util.Locale
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {
    private val themeManager: ThemeManager by inject()
    private val presentationConfig: IPresentationConfig by inject()
    private val firebaseManager: FirebaseManager by inject()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AndroidGpsEnabler.handleActivityResult(requestCode, resultCode)
    }
}

@Composable
private fun AppContent() {
    val themeManager: ThemeManager = koinInject()
    val currentLanguage by themeManager.currentLanguage.collectAsState()
    key(currentLanguage) {
        AppTheme(themeManager = themeManager) {
            AppRouter()
        }
    }
}

@Composable
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 800, heightDp = 400)
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

    DiscountScreen_Sample(themeManager)
    //ProductListScreen_Sample(themeManager)

}