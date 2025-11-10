@file:OptIn(ExperimentalTime::class)

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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.repzone.core.di.CoreModule
import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.di.CoreUiModule
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.data.di.RepositoryModulePreview
import com.repzone.database.di.DatabaseAndroidPreviewModule
import com.repzone.database.di.DatabaseModulePreview
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.di.FirebaseMockAndroidModule
import com.repzone.navigation.AppRouter
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.presentation.legacy.di.PresentationModuleLegacy
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.ui.visit.VisitActionList
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
    key(currentLanguage) {
        AppTheme(themeManager = themeManager) {
            //AppRouter()
            AppAndroidPreview()
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
    val lists =  listOf(
        VisitActionItem(
            name = "Sipariş Oluştur",
            description = "Yeni sipariş kaydı oluşturun",
            documentType = DocumentTypeGroup.ORDER,
            isMandatory = true,
            hasDone = false,
            displayOrder = 1,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Acil Sipariş",
            description = "Acil sipariş kaydı",
            documentType = DocumentTypeGroup.ORDER,
            isMandatory = false,
            hasDone = true,
            displayOrder = 2,
            interval = TaskRepeatInterval.WEEK
        ),
        VisitActionItem(
            name = "Fatura Kes",
            description = "Müşteriye fatura düzenleyin",
            documentType = DocumentTypeGroup.INVOICE,
            hasDone = false,
            displayOrder = 3,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Proforma Fatura",
            description = "Proforma fatura düzenleyin",
            documentType = DocumentTypeGroup.INVOICE,
            hasDone = true,
            displayOrder = 4,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Sevkiyat Hazırla",
            description = "Sevkiyat belgesi hazırlayın",
            documentType = DocumentTypeGroup.DISPATCH,
            hasDone = false,
            isFulfillment = true,
            displayOrder = 5,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Müşteri Formu",
            description = "Müşteri bilgi formunu doldurun",
            documentType = DocumentTypeGroup.FORM,
            interval = TaskRepeatInterval.ONE_TIME,
            isMandatory = true,
            hasDone = false,
            displayOrder = 6
        ),
        VisitActionItem(
            name = "Memnuniyet Anketi",
            description = "Müşteri memnuniyet anketini doldurun",
            documentType = DocumentTypeGroup.FORM,
            hasDone = false,
            displayOrder = 7,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Tahsilat Yap",
            description = "Müşteriden tahsilat yapın",
            documentType = DocumentTypeGroup.COLLECTION,
            hasDone = false,
            displayOrder = 8,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Depo Fişi",
            description = "Depo giriş fişi oluşturun",
            documentType = DocumentTypeGroup.WAREHOUSERECEIPT,
            hasDone = true,
            displayOrder = 9,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Diğer İşlem",
            description = "Diğer işlemleri gerçekleştirin",
            documentType = DocumentTypeGroup.OTHER,
            hasDone = false,
            displayOrder = 10,
            interval = TaskRepeatInterval.ATVISITSTART
        )
    )
    AppTheme(themeManager = themeManager) {
        VisitActionList(
            items = lists,
            onItemClick = {

            },
            themeManager = themeManager,
            modifier = Modifier
        )
    }

}