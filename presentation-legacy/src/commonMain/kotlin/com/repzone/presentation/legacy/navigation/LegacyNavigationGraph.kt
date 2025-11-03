package com.repzone.presentation.legacy.navigation

import kotlinx.serialization.Serializable

sealed interface LegacyScreen {

    // ============ GRAPH ROUTES ============
    @Serializable
    data object AuthGraph : LegacyScreen

    @Serializable
    data object MainGraph : LegacyScreen

    // ============ AUTH SCREENS ============
    @Serializable
    data object Login : LegacyScreen

    @Serializable
    data object Splash : LegacyScreen

    @Serializable
    data object TestScreen : LegacyScreen

    @Serializable
    data class ForgotPassword(val email: String? = null) : LegacyScreen

    // ============ MAIN SCREENS ============
    @Serializable
    data object Sync : LegacyScreen
    @Serializable
    data object CustomerList : LegacyScreen

    @Serializable
    data object SettingsScreen: LegacyScreen
}