package com.repzone.core.ui.manager.theme.common

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

interface IColorPalet {

    //region Primary Palette (Orange)
    val primary0: Color
    val primary5: Color
    val primary10: Color
    val primary15: Color
    val primary20: Color
    val primary25: Color
    val primary30: Color
    val primary35: Color
    val primary40: Color
    val primary50: Color
    val primary60: Color
    val primary70: Color
    val primary80: Color
    val primary90: Color
    val primary95: Color
    val primary99: Color
    val primary100: Color

    val white: Color
    val black: Color
    //endregion Primary Palette (Orange)

    //region Secondary Palette
    val secondary0: Color
    val secondary5: Color
    val secondary10: Color
    val secondary15: Color
    val secondary20: Color
    val secondary25: Color
    val secondary30: Color
    val secondary35: Color
    val secondary40: Color
    val secondary50: Color
    val secondary60: Color
    val secondary70: Color
    val secondary80: Color
    val secondary90: Color
    val secondary95: Color
    val secondary99: Color
    val secondary100: Color
    //endregion Secondary Palette

    //region Tertiary Palette
    val tertiary0: Color
    val tertiary5: Color
    val tertiary10: Color
    val tertiary15: Color
    val tertiary20: Color
    val tertiary25: Color
    val tertiary30: Color
    val tertiary35: Color
    val tertiary40: Color
    val tertiary50: Color
    val tertiary60: Color
    val tertiary70: Color
    val tertiary80: Color
    val tertiary90: Color
    val tertiary95: Color
    val tertiary99: Color
    val tertiary100: Color
    //endregion Tertiary Palette

    //region Neutral Palette
    val neutral0: Color
    val neutral5: Color
    val neutral10: Color
    val neutral15: Color
    val neutral20: Color
    val neutral25: Color
    val neutral30: Color
    val neutral35: Color
    val neutral40: Color
    val neutral50: Color
    val neutral60: Color
    val neutral70: Color
    val neutral80: Color
    val neutral90: Color
    val neutral95: Color
    val neutral99: Color
    val neutral100: Color
    //endregion Neutral Palette

    //region Neutral Variant Palette
    val neutralVariant0: Color
    val neutralVariant5: Color
    val neutralVariant10: Color
    val neutralVariant15: Color
    val neutralVariant20: Color
    val neutralVariant25: Color
    val neutralVariant30: Color
    val neutralVariant35: Color
    val neutralVariant40: Color
    val neutralVariant50: Color
    val neutralVariant60: Color
    val neutralVariant70: Color
    val neutralVariant80: Color
    val neutralVariant90: Color
    val neutralVariant95: Color
    val neutralVariant99: Color
    val neutralVariant100: Color
    //endregion Neutral Variant Palette

    //region Error Palette
    val error0: Color
    val error5: Color
    val error10: Color
    val error15: Color
    val error20: Color
    val error25: Color
    val error30: Color
    val error35: Color
    val error40: Color
    val error50: Color
    val error60: Color
    val error70: Color
    val error80: Color
    val error90: Color
    val error95: Color
    val error99: Color
    val error100: Color
    //endregion Error Palette

    //region Aditional Palette
    val startVisitIconBackGround : Color
    val stopVisitIconBackGround : Color
    //endregion Aditional Palette

    fun lightColorScheme(): ColorScheme
    fun darkColorScheme(): ColorScheme
}