package com.repzone.mobile

import androidx.compose.ui.window.ComposeUIViewController
import com.repzone.navigation.AppRouter

fun MainViewController() = ComposeUIViewController { AppRouter() }