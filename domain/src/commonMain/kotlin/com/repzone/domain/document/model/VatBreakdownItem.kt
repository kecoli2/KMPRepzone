package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class VatBreakdownItem(val rate: BigDecimal, val baseAmount: BigDecimal, val vatAmount: BigDecimal)
