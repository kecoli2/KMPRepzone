package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class ProductUnit(
    val unitId: Int,
    val unitName: String,
    val price: BigDecimal, // KDV HARİÇ fiyat mı yok sa kdv dahilmi onu soracagım
    val priceIncludesVat: Boolean = false,  // Fiyat KDV dahil mi? parametresi kullanılacak mıdır
    val vat: BigDecimal,
    val multiplier: BigDecimal,
    val weight: Double? = null,
    val minimumOrderQuantity: Int?,
    val maxOrderQuantity: Int?,
    val orderQuantityFactor: Int,
    val isBaseUnit: Boolean,
    val unitDisplayOrder: Int,
    val barcode: String?,
    val productId: Long
)