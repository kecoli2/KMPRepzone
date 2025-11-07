package com.repzone.presentation.legacy.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.repzone.core.ui.component.selectiondialog.sample.ExampleUsageScreen
import com.repzone.core.ui.ui.settings.SettingsScreen
import com.repzone.core.ui.util.enum.NavigationItemType
import com.repzone.presentation.legacy.navigation.NavigationSharedStateHolder
import com.repzone.presentation.legacy.ui.login.LoginScreenLegacy
import com.repzone.presentation.legacy.ui.customerlist.CustomerListScreenLegacy
import com.repzone.presentation.legacy.ui.splash.SplashScreenLegacy
import com.repzone.presentation.legacy.ui.sync.SyncScreenLegacy
import com.repzone.presentation.legacy.ui.visit.VisitScreenLegacy
import org.koin.compose.koinInject

@Composable
fun LegacyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = LegacyScreen.AuthGraph,
            modifier = modifier.fillMaxSize(),
            // ... transitions ...
        ) {

            // ============ AUTH GRAPH ============
            navigation<LegacyScreen.AuthGraph>(startDestination = LegacyScreen.Splash  //
            ) {
                composable<LegacyScreen.Splash> {
                    SplashScreenLegacy(
                        onNavigateToLogin = {
                            navController.navigate(LegacyScreen.Login) {
                                popUpTo(LegacyScreen.Splash) { inclusive = true }
                            }
                        },
                        onNavigateToMain = {
                            navController.navigate(LegacyScreen.MainGraph) {
                                popUpTo(LegacyScreen.AuthGraph) { inclusive = true }
                            }
                        }
                    )
                }

                composable<LegacyScreen.Login> {
                    LoginScreenLegacy(
                        onLoginSuccess = {
                            // Login başarılı → Main graph'a git
                            navController.navigate(LegacyScreen.MainGraph) {
                                popUpTo(LegacyScreen.AuthGraph) { inclusive = true }
                            }
                        }
                    )
                }

                composable<LegacyScreen.TestScreen> {
                    ExampleUsageScreen()
                }
            }

            // ============ MAIN APP GRAPH ============
            navigation<LegacyScreen.MainGraph>(startDestination = LegacyScreen.Sync) {
                composable<LegacyScreen.Sync> {
                    SyncScreenLegacy(
                        onSyncCompleted = {
                            navController.navigate(LegacyScreen.CustomerList) {
                                popUpTo(LegacyScreen.Sync) { inclusive = true }
                            }
                        }
                    )
                }

                composable<LegacyScreen.CustomerList> {
                    val sharedHolder : NavigationSharedStateHolder = koinInject()
                    CustomerListScreenLegacy({ type ->
                        when(type) {
                            NavigationItemType.GENERAL_SETTINGS -> {
                                navController.navigate(LegacyScreen.SettingsScreen)
                            }

                            NavigationItemType.LOGOUT -> {
                                navController.navigate(LegacyScreen.Login) {
                                    // Tüm back stack'i temizle
                                    popUpTo(0) { inclusive = true }
                                }
                            }

                            else -> { /* TODO */ }
                        }
                    }, onCustomerClick = {
                        sharedHolder.setSelectedActiveCustomer(it)
                        navController.navigate(LegacyScreen.VisitScreen)
                    })
                }

                composable<LegacyScreen.SettingsScreen> {
                    SettingsScreen {
                        navController.navigateUp()
                    }
                }

                composable<LegacyScreen.VisitScreen> {
                    val navigationState: NavigationSharedStateHolder = koinInject()
                    val selectedCustomer by navigationState.selectedCustomer.collectAsState()

                    selectedCustomer?.let {
                        VisitScreenLegacy(it,
                            onBackClick = {
                                navigationState.clearCustomer()
                                navController.navigateUp()
                            })
                    }
                }
            }
        }
    }
}