package com.repzone.presentation.legacy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.repzone.presentation.legacy.ui.login.LoginScreenLegacy
import com.repzone.presentation.legacy.ui.sync.SyncTestScreenLegacy

@Composable
fun LegacyNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
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
        navigation<LegacyScreen.AuthGraph>(startDestination = LegacyScreen.Login) {
            composable<LegacyScreen.Login> {
                LoginScreenLegacy(
                    onLoginSuccess = {
                        // Main graph'a git
                        navController.navigate(LegacyScreen.MainGraph) {
                            popUpTo<LegacyScreen.AuthGraph> { inclusive = true }
                        }
                    }
                )
            }

            composable<LegacyScreen.ForgotPassword> {
                val args = it.toRoute<LegacyScreen.ForgotPassword>()
                /* ForgotPasswordScreen(
                     initialEmail = args.email,
                     onBackClick = { navController.navigateUp() },
                     onResetSuccess = { navController.navigateUp() }
                 )*/
            }
        }

        // ============ MAIN APP GRAPH ============
        navigation<LegacyScreen.MainGraph>(startDestination = LegacyScreen.Sync) {
            composable<LegacyScreen.Sync> {
                SyncTestScreenLegacy()
            }
        }
    }
}