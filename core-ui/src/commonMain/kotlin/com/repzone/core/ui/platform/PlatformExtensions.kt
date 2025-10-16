package com.repzone.core.ui.platform

import androidx.compose.runtime.Composable

@Composable
expect fun HandleBackPress(onBack: () -> Unit)