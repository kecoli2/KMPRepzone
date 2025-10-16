package com.repzone.core.ui.platform

import androidx.compose.runtime.Composable

@Composable
actual fun HandleBackPress(onBack: () -> Unit) {
    androidx.activity.compose.BackHandler { onBack() }
}