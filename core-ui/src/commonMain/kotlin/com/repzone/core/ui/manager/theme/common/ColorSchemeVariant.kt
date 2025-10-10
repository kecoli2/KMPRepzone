package com.repzone.core.ui.manager.theme.common

import com.repzone.core.config.ThemeType

data class ColorSchemeVariant(
    val id: ThemeType,                    // "orange", "red", "yellow"
    val name: String,                  // "Turuncu Tema", "Kırmızı Tema"
    val lightColorScheme: Any,         // ColorScheme
    val darkColorScheme: Any,          // ColorScheme
    val typography: Any? = null,       // Typography
    val shapes: Any? = null            // Shapes
)