package com.repzone.core.ui.manager.theme.common

import com.repzone.core.enums.ThemeType

data class ColorSchemeVariant(
    val id: ThemeType,                 // "orange", "red", "yellow"
    val name: String,                  // "Turuncu Tema", "Kırmızı Tema"
    val typography: Any? = null,       // Typography
    val shapes: Any? = null,           // Shapes
    val colorPalet: IColorPalet        // color Paletleri alacak tümü
)