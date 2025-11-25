package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Miktar girişi için doğrulama durumu
 */
sealed class ValidationStatus {
    object Empty : ValidationStatus()
    object Valid : ValidationStatus()
    data class Warning(val message: String, val availableStock: BigDecimal? = null) : ValidationStatus()
    data class Error(val message: String) : ValidationStatus()
}