package com.repzone.presentation.legacy.navigation

import com.repzone.domain.model.CustomerItemModel
import kotlinx.serialization.Serializable

sealed interface LegacyScreen {

    //region ============ GRAPH ROUTES ============
    @Serializable
    data object AuthGraph : LegacyScreen

    @Serializable
    data object MainGraph : LegacyScreen

    @Serializable
    data object DocumentGraph : LegacyScreen
    //endregion ============ GRAPH ROUTES ============

    //region ============ AUTH SCREENS ============
    @Serializable
    data object Login : LegacyScreen
    @Serializable
    data object Splash : LegacyScreen
    @Serializable
    data class ForgotPassword(val email: String? = null) : LegacyScreen
    @Serializable
    data object GpsTrackingScreen : LegacyScreen
    //endregion ============ AUTH SCREENS ============

    //region ============ MAIN SCREENS ============
    @Serializable
    data object Sync : LegacyScreen
    @Serializable
    data object CustomerList : LegacyScreen
    @Serializable
    data object SettingsScreen: LegacyScreen
    @Serializable
    data object VisitScreen: LegacyScreen
    //endregion ============ MAIN SCREENS ============

    //region ============ DOCUMENT SCREENS ============
    @Serializable
    data object ProductList: LegacyScreen
/*    @Serializable
    data object DocumentSettings: LegacyScreen*/
    @Serializable
    data object DocumentBasket: LegacyScreen
    //endregion ============ DOCUMENT SCREENS ============

}