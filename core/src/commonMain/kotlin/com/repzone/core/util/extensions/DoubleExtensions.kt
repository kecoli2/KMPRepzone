package com.repzone.core.util.extensions

import com.repzone.core.platform.CurrencyFormatter


fun Double?.toMoney(): String {
    val formatter = CurrencyFormatter()
    return formatter.format(this ?: 0.0)
}