package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Ürün
 */
data class Product(
    val id: String,
    val name: String,
    val code: String,
    val groupId: String?,
    val tags: List<String> = emptyList(),
    val stockQuantity: BigDecimal,
    val stockUnitId: String,
    val units: List<ProductUnit>
) {
    /**
     * Base birim (stok bu birimde tutuluyor)
     */
    val baseUnit: ProductUnit
        get() = units.first { it.isBaseUnit }
}

/**
 * Ürün birimi
 */
data class ProductUnit(
    val id: String,
    val productId: String,
    val unitId: String,
    val unitName: String,
    val conversionFactor: BigDecimal,
    val isBaseUnit: Boolean,
    val price: BigDecimal,
    val barcode: String?
)
