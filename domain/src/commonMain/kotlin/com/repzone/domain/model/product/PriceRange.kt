package com.repzone.domain.model.product

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Fiyat aralıkları
 */
data class PriceRange(val min: BigDecimal? = null, val max: BigDecimal? = null) {
    val isValid: Boolean
        get() = (min != null || max != null) &&
                (min == null || max == null || min <= max)
}