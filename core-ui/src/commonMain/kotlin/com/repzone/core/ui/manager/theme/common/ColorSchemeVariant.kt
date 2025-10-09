package com.repzone.core.ui.manager.theme.common

data class ColorSchemeVariant(
    val id: String,                    // "orange", "red", "yellow"
    val name: String,                  // "Turuncu Tema", "Kırmızı Tema"
    val lightColorScheme: Any,         // ColorScheme
    val darkColorScheme: Any,          // ColorScheme
    val typography: Any? = null,       // Typography
    val shapes: Any? = null            // Shapes
)