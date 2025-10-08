package com.repzone.presentation.navigation

import kotlinx.serialization.Serializable
sealed interface Screen {

    // ============ GRAPH ROUTES ============
    @Serializable
    data object AuthGraph : Screen

    @Serializable
    data object MainGraph : Screen

    // ============ AUTH SCREENS ============
    @Serializable
    data object Login : Screen

    @Serializable
    data class ForgotPassword(val email: String? = null) : Screen

    // ============ MAIN SCREENS ============
    @Serializable
    data object Sync : Screen
}