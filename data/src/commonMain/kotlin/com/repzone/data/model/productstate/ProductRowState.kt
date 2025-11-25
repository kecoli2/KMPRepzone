package com.repzone.data.model.productstate

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.ValidationStatus

/**
 * Listedeki tek bir ürün satırı için UI durumu.
 * Bu yapı, filtreleme/arama işlemlerinde durumu korumak için PagingData'dan ayrı tutulur.
 */
data class ProductRowState(
    val productId: String,

    // Birim döngüsü
    val currentUnitIndex: Int = 0,
    val availableUnits: List<ProductUnit> = emptyList(),

    // Miktar girişi
    val quantityText: String = "",

    // İndirim durumu
    val hasDiscount: Boolean = false,
    val discountSlots: List<DiscountSlotEntry> = emptyList(),

    // Doğrulama
    val validationStatus: ValidationStatus = ValidationStatus.Empty,

    // Doküman takibi
    val isInDocument: Boolean = false,
    val documentQuantity: BigDecimal = BigDecimal.ZERO,
    val documentUnitId: String? = null,      // unitId
    val documentUnitName: String? = null     // unitName
) {
    /**
     * Seçili mevcut birimi getirir
     */
    val currentUnit: ProductUnit?
        get() = availableUnits.getOrNull(currentUnitIndex)

    /**
     * Girilen miktarın geçerli olup olmadığını kontrol eder
     */
    val isValidQuantity: Boolean
        get() = quantityText.toBigDecimalOrNull() != null &&
                quantityText.toBigDecimalOrNull()!! > BigDecimal.ZERO

    /**
     * Satırın dokümana eklenip eklenemeyeceğini kontrol eder
     */
    val canAddToDocument: Boolean
        get() = isValidQuantity &&
                validationStatus !is ValidationStatus.Error &&
                currentUnit != null
}

