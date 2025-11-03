@file:OptIn(ExperimentalTime::class)

package com.repzone.mobile

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.repzone.core.di.CoreModule
import com.repzone.core.ui.component.selectiondialog.GenericPopupList
import com.repzone.core.ui.component.selectiondialog.SelectionMode
import com.repzone.core.ui.component.selectiondialog.sample.CustomerRow
import com.repzone.core.ui.component.selectiondialog.sample.CustomerSample
import com.repzone.core.ui.component.selectiondialog.sample.ExampleUsageScreen
import com.repzone.core.ui.component.selectiondialog.sample.ProductSample
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.di.CoreUiModule
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.data.di.RepositoryModulePreview
import com.repzone.database.di.DatabaseAndroidPreviewModule
import com.repzone.database.di.DatabaseModulePreview
import com.repzone.domain.model.CustomerItemModel
import com.repzone.mobile.di.AndroidDIModulePreview
import com.repzone.mobile.di.FirebaseMockAndroidModule
import com.repzone.navigation.AppRouter
import com.repzone.network.di.NetworkModule
import com.repzone.network.di.PlatformNetworkModule
import com.repzone.presentation.legacy.di.PresentationModuleLegacy
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.ui.visit.VisitScreenLegacy
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
/*        CustomerCard(customer = CustomerItemModel(
            customerId = 1,
            visitId = 1,
            iconIndex = null,
            finishDate = null,
            appointmentId = 1,
            date = null,
            tagRaw = null,
            name = "Salih Yücel",
            customerCode = "1",
            customerGroupName = "Migros",
            address = "TODO()",
            latitude = null,
            longitude = null,
            addressType = null,
            imageUri = null,
            parentCustomerId = 1,
            endDate = null,
            customerBlocked = 1,
            sprintId = 1,
            dontShowDatePart = false,
            swipeEnabled = false,
            showCalendarInfo = false,
            showDisplayClock = false,
            displayOrder = 1,
            showDisplayOrder = false,
        ), modifier = Modifier ,themeManager = themeManager)*/
         /*VisitScreenLegacy(customer = CustomerItemModel(
             customerId = 1,
             visitId = 1,
             iconIndex = null,
             finishDate = null,
             appointmentId = 1,
             date = null,
             tagRaw = null,
             name = "Salih Yücel",
             customerCode = "1",
             customerGroupName = "Migros",
             address = "TODO()",
             latitude = null,
             longitude = null,
             addressType = null,
             imageUri = null,
             parentCustomerId = 1,
             endDate = null,
             customerBlocked = 1,
             sprintId = 1,
             dontShowDatePart = false,
             swipeEnabled = false,
             showCalendarInfo = false,
             showDisplayClock = false,
             displayOrder = 1,
             showDisplayOrder = false)
         )*/
        var showCustomerDialog by remember { mutableStateOf(true) }
        var showProductBottomSheet by remember { mutableStateOf(false) }
        var selectedCustomer by remember { mutableStateOf<CustomerSample?>(null) }
        var selectedProducts by remember { mutableStateOf<List<ProductSample>>(emptyList()) }
        val customers = remember {
            listOf(
                CustomerSample(1, "Ahmet Yılmaz", "ahmet@example.com", "+90 532 123 4567"),
                CustomerSample(2, "Ayşe Demir", "ayse@example.com", "+90 533 234 5678"),
                CustomerSample(3, "Mehmet Kaya", "mehmet@example.com", "+90 534 345 6789"),
                CustomerSample(4, "Fatma Şahin", "fatma@example.com", "+90 535 456 7890"),
                CustomerSample(5, "Ali Çelik", "ali@example.com", "+90 536 567 8901")
            )
        }
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            color = themeManager.getCurrentColorScheme().colorPalet.secondary20

        ) {
            GenericPopupList(
                items = customers,
                selectionMode = SelectionMode.SINGLE,
                selectedItems = selectedCustomer?.let { listOf(it) },
                itemContent = { customer, isSelected ->
                    CustomerRow(customer = customer, isSelected = isSelected)
                },
                itemKey = { it.id },
                searchEnabled = true,
                searchPredicate = { customer, query ->
                    customer.name.contains(query, ignoreCase = true) ||
                            customer.email.contains(query, ignoreCase = true) ||
                            customer.phone.contains(query, ignoreCase = true)
                },
                searchPlaceholder = "Müşteri ara...",
                confirmButtonText = "Seç",
                cancelButtonText = "İptal",
                onConfirm = { selected ->
                    selectedCustomer = selected.firstOrNull()
                    showCustomerDialog = false
                },
                onDismiss = { showCustomerDialog = false }
            )
        }

    }
}