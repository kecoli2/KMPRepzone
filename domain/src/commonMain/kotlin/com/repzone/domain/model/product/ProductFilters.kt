package com.repzone.domain.model.product

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Mevcut filtre seçenekleri (tüm ürünlerden alınan benzersiz değerler)
 */
data class ProductFilters(
    val brands: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val priceMin: BigDecimal? = null,
    val priceMax: BigDecimal? = null
)
