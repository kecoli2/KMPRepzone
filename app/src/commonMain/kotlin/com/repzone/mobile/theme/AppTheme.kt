package com.repzone.mobile.thema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.repzone.mobile.theme.AppDarkColors
import com.repzone.mobile.theme.AppLightColors


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) AppDarkColors else AppLightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(), // isterseniz özelleştirebiliriz
        content = content
    )
}