package com.repzone.core.ui.manager.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSUserDefaults

@Composable
actual fun rememberLocaleConfiguration(languageCode: String): AppLocale {
    return remember(languageCode) {
        // iOS için locale ayarla
        val defaults = NSUserDefaults.standardUserDefaults
        defaults.setObject(listOf(languageCode), forKey = "AppleLanguages")
        defaults.synchronize()

        AppLocale(languageCode)
    }
}