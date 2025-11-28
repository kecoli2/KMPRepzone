package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class DiscountDialogState(
    val productId: String,
    val product: ProductInformationModel,
    val currentUnit: ProductUnit,
    val quantity: BigDecimal,
    val existingDiscounts: List<DiscountSlotEntry>
)