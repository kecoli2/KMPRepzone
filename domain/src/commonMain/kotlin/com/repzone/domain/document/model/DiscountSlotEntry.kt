package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal


/**
 * Bir ürün için indirim slot kaydı
 */
data class DiscountSlotEntry(
    val slotNumber: Int,
    val value: String = "",  // TextField değeri (kullanıcı girişi)
    val type: DiscountType = DiscountType.PERCENTAGE,
    val isEnabled: Boolean = true,
    val validationError: String? = null,
    val maximumValue: BigDecimal? = null
)
