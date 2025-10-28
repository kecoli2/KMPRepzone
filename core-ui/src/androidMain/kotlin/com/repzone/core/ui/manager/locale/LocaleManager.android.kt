package com.repzone.core.ui.manager.locale

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import java.util.Locale

@Composable
actual fun rememberLocaleConfiguration(languageCode: String): AppLocale {
    val context = LocalContext.current

    return remember(languageCode) {
        updateLocale(context, languageCode)
        AppLocale(languageCode)
    }
}

private fun updateLocale(context: Context, languageCode: String) {
    // Modern yöntem - AppCompatDelegate kullan
    val localeList = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(localeList)
}