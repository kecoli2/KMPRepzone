package com.repzone.presentation.legacy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.repzone.presentation.legacy.ui.login.LoginScreenLegacy
import com.repzone.presentation.legacy.ui.customerlist.CustomerListScreenLegacy
import com.repzone.presentation.legacy.ui.splash.SplashScreenLegacy
import com.repzone.presentation.legacy.ui.sync.SyncScreenLegacy

@Composable
fun LegacyNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = LegacyScreen.AuthGraph,
            modifier = modifier.fillMaxSize(),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            }
        ) {
         // ============ AUTH GRAPH ============
         navigation<LegacyScreen.AuthGraph>(startDestination = LegacyScreen.Splash) {
                composable<LegacyScreen.Splash> {
                    SplashScreenLegacy(
                        onControllSucces = {
                            // Main graph'a git
                            navController.navigate(LegacyScreen.Sync) {
                                popUpTo<LegacyScreen.AuthGraph> { inclusive = false }
                            }
                        }
                    )
                }
                composable<LegacyScreen.Login> {
                    LoginScreenLegacy(
                        onLoginSuccess = {
                            // Main graph'a git
                            navController.navigate(LegacyScreen.Sync) {
                                popUpTo<LegacyScreen.AuthGraph> { inclusive = true }
                            }
                        }
                    )
                }

             composable<LegacyScreen.Sync> {
                 SyncScreenLegacy(
                     onSyncCompleted = {
                         navController.navigate(LegacyScreen.CustomerList) {
                             popUpTo<LegacyScreen.Sync> { inclusive = true }
                         }
                     }

                 )
             }
            }

         // ============ MAIN APP GRAPH ============
         navigation<LegacyScreen.MainGraph>(startDestination = LegacyScreen.CustomerList) {
               composable<LegacyScreen.CustomerList> {
                     CustomerListScreenLegacy()
               }
         }
        }
    }
}