package com.repzone.presentation.legacy.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}