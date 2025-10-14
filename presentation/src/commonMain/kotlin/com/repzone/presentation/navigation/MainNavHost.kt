package com.repzone.presentation.navigation

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
import androidx.navigation.toRoute
import com.repzone.presentation.ui.login.LoginScreen
import com.repzone.presentation.ui.sync.SyncTestScreen

@Composable
fun MainNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    CompositionLocalProvider(LocalNavController provides navController){
        NavHost(
            navController = navController,
            startDestination = Screen.AuthGraph,
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
            navigation<Screen.AuthGraph>(startDestination = Screen.Login) {
                composable<Screen.Login> {
                    LoginScreen(
                        onLoginSuccess = {
                            // Main graph'a git
                            navController.navigate(Screen.MainGraph) {
                                popUpTo<Screen.AuthGraph> { inclusive = true }
                            }
                        },
                        onForgotPasswordClick = { email ->
                            navController.navigate(Screen.ForgotPassword(email))
                        }
                    )
                }

                composable<Screen.ForgotPassword> {
                    val args = it.toRoute<Screen.ForgotPassword>()
                    /* ForgotPasswordScreen(
                         initialEmail = args.email,
                         onBackClick = { navController.navigateUp() },
                         onResetSuccess = { navController.navigateUp() }
                     )*/
                }
            }

            // ============ MAIN APP GRAPH ============
            navigation<Screen.MainGraph>(startDestination = Screen.Sync) {
                composable<Screen.Sync> {
                    SyncTestScreen(onBackClick = { navController.navigateUp() })
                }
            }
        }
    }
}