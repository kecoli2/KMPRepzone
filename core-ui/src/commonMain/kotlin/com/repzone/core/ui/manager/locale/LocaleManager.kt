package com.repzone.core.ui.manager.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

data class AppLocale(
    val languageCode: String
)

val LocalAppLocale = compositionLocalOf { AppLocale("en") }

@Composable
expect fun rememberLocaleConfiguration(languageCode: String): AppLocale

