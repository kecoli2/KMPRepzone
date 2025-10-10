package com.repzone.navigation

import androidx.compose.runtime.Composable
import com.repzone.core.config.BuildConfig
import com.repzone.core.enums.UIModule

/**
 * Main app navigation router
 * Delegates to the appropriate UI module based on BuildConfig
 */
@Composable
fun AppRouter() {
    when (BuildConfig.activeUIModule) {
        UIModule.NEW -> {
            // New UI navigation
            com.repzone.presentation.navigation.MainNavHost()
        }
        UIModule.LEGACY -> {
            com.repzone.presentation.legacy.navigation.LegacyNavHost()
        }
    }
}