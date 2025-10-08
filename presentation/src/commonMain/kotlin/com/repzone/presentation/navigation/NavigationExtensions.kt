package com.repzone.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Navigate and clear back stack
 */
fun NavController.navigateAndClearBackStack(route: Screen) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}

/**
 * Navigate with single top
 */
fun NavController.navigateSingleTop(route: Screen) {
    navigate(route) {
        launchSingleTop = true
    }
}

/**
 * Navigate and pop up to route
 */
fun NavController.navigateAndPopUpTo(route: Screen, popUpToRoute: Screen, inclusive: Boolean = false) {
    navigate(route) {
        popUpTo(popUpToRoute) {
            this.inclusive = inclusive
        }
    }
}

/**
 * Safe navigation - checks if destination exists
 */
fun NavController.navigateSafe(route: Screen, builder: NavOptionsBuilder.() -> Unit = {}) {
    try {
        navigate(route, builder)
    } catch (e: Exception) {
        // Log error or handle gracefully
        println("Navigation error: ${e.message}")
    }
}