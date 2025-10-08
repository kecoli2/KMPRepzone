package com.repzone.navigation

import androidx.compose.runtime.Composable
import com.repzone.core.config.BuildConfig
import com.repzone.core.config.UIModule
import com.repzone.presentation.navigation.MainNavHost
import com.repzone.presentation.navigation.Screen

/**
 * Main app navigation router
 * Delegates to the appropriate UI module based on BuildConfig
 */
@Composable
fun AppRouter() {
    when (BuildConfig.activeUIModule) {
        UIModule.NEW -> {
            // New UI navigation
            MainNavHost()
        }
        UIModule.LEGACY -> {
            // Legacy UI navigation
            //LegacyUINavigation()
        }
    }
}