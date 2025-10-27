package com.repzone.core.ui.manager.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Tüm boyutları tutan data class
data class Dimensions(
    // Padding değerleri
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val paddingExtraLarge: Dp,

    // Font boyutları
    val textSmall: TextUnit,
    val textMedium: TextUnit,
    val textLarge: TextUnit,
    val textExtraLarge: TextUnit,

    // Icon boyutları
    val iconSizeSmall: Dp,
    val iconSizeMedium: Dp,
    val iconSizeLarge: Dp,

    // Component boyutları
    val buttonHeight: Dp,
    val cornerRadius: Dp
)

// Her ekran boyutu için hazır değerler
object DimensionDefaults {
    // Telefon (Compact)
    val compact = Dimensions(
        paddingSmall = 8.dp,
        paddingMedium = 16.dp,
        paddingLarge = 24.dp,
        paddingExtraLarge = 32.dp,

        textSmall = 12.sp,
        textMedium = 16.sp,
        textLarge = 20.sp,
        textExtraLarge = 24.sp,

        iconSizeSmall = 20.dp,
        iconSizeMedium = 24.dp,
        iconSizeLarge = 32.dp,

        buttonHeight = 48.dp,
        cornerRadius = 12.dp
    )

    // Tablet Dikey (Medium)
    val medium = Dimensions(
        paddingSmall = 12.dp,
        paddingMedium = 20.dp,
        paddingLarge = 32.dp,
        paddingExtraLarge = 48.dp,

        textSmall = 14.sp,
        textMedium = 18.sp,
        textLarge = 24.sp,
        textExtraLarge = 28.sp,

        iconSizeSmall = 24.dp,
        iconSizeMedium = 28.dp,
        iconSizeLarge = 40.dp,

        buttonHeight = 56.dp,
        cornerRadius = 16.dp
    )

    // Tablet Yatay (Expanded)
    val expanded = Dimensions(
        paddingSmall = 16.dp,
        paddingMedium = 24.dp,
        paddingLarge = 40.dp,
        paddingExtraLarge = 56.dp,

        textSmall = 16.sp,
        textMedium = 20.sp,
        textLarge = 28.sp,
        textExtraLarge = 32.sp,

        iconSizeSmall = 28.dp,
        iconSizeMedium = 32.dp,
        iconSizeLarge = 48.dp,

        buttonHeight = 64.dp,
        cornerRadius = 20.dp
    )
}