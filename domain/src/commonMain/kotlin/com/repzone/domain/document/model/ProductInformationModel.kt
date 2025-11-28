package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Ürün
 */
data class ProductInformationModel(
    val id: Int,
    val name: String,
    val sku: String,
    val vat: BigDecimal,
    val tags: List<String> = emptyList(),
    val brandId: Int,
    val brandName: String? = null,
    val groupId: Int,
    val groupName: String,
    val stock: BigDecimal,
    val orderStock: BigDecimal? = null,
    val vanStock: BigDecimal? = null,
    val transitStock: BigDecimal? = null,
    val photoPath: String? = null,
    val defaultUnitMultiplier: BigDecimal,
    val defaultUnitName: String,
    val defaultUnitWeight: BigDecimal? = null,
    val units: List<ProductUnit>,
    val color: String = "#ffffff",
    val brandPhotoPath: String? = null,
    val groupPhotoPath: String? = null,
    val displayOrder: Int = 0,
    val description: String,
    val pendingStock: BigDecimal? = null,
    val reservedStock: BigDecimal? = null,
    val showAvailableStock: Boolean = false,
    val showTransitStock: Boolean = false,
    val manufacturerId:Int? = null
) {
    /**
     * Base birim (stok bu birimde tutuluyor)
     */
    val baseUnit: ProductUnit
        get() = units.first { it.isBaseUnit }
}


