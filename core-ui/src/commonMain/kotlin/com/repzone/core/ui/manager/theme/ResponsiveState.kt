package com.repzone.core.ui.manager.theme

data class ResponsiveState(
    val windowSizeClass: WindowSizeClass = WindowSizeClass(
        widthSizeClass = WindowWidthSizeClass.Compact,
        heightSizeClass = WindowHeightSizeClass.Medium
    ),
    val isLandscape: Boolean = false,
    val dimensions: Dimensions = DimensionDefaults.compact
)