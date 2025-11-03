package com.repzone.core.util.extensions

import com.repzone.core.platform.CurrencyFormatter


fun Double?.toMoney(): String {
    val formatter = CurrencyFormatter()
    val ss = formatter.format(this ?: 0.0)
    return ss
}