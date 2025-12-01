package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class DiscountSlotEntry(
    val slotNumber: Int,
    val value: String = "",
    val type: DiscountType = DiscountType.PERCENTAGE,
    val isEnabled: Boolean = true,
    val validationError: String? = null,
    val maximumValue: BigDecimal? = null
)
