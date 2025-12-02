package com.repzone.core.util.extensions

import com.ionspin.kotlin.bignum.decimal.BigDecimal

fun BigDecimal.toCleanString(): String {
    val str = this.toPlainString()
    if (!str.contains(".")) return str
    return str.trimEnd('0').trimEnd('.')
}