package com.repzone.mobile.thema

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(dark: Boolean = false,   content: @Composable () -> Unit) {
    val colors =
        if (dark) darkColorScheme()
        else lightColorScheme()

    MaterialTheme(colorScheme = colors, typography = Typography(), content = content)
}