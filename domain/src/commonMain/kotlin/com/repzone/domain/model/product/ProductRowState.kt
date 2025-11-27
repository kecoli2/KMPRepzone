package com.repzone.domain.model.product

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.document.model.DiscountSlotEntry
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

    // Kaydedilmiş birim girişleri (birim değiştiğinde otomatik kaydedilir)
    val unitEntries: Map<String, UnitEntry> = emptyMap(),

    // Doküman takibi
    val isInDocument: Boolean = false,
    val documentQuantity: BigDecimal = BigDecimal.ZERO,
    val documentUnitId: String? = null,
    val documentUnitName: String? = null
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
        get() = quantityText.isNotEmpty() &&
                quantityText.toBigDecimalOrNull() != null &&
                quantityText.toBigDecimalOrNull()!! > BigDecimal.ZERO

    /**
     * Bu ürün için herhangi bir giriş var mı?
     */
    val hasAnyEntry: Boolean
        get() = unitEntries.isNotEmpty() ||
                (isValidQuantity && validationStatus !is ValidationStatus.Error)

    /**
     * Tüm girişlerin özeti (badge için)
     * Örn: "1 Adet, 5 Koli"
     */
    val entrySummary: String
        get() {
            val entries = mutableListOf<String>()

            unitEntries.values.forEach { entry ->
                entries.add("${entry.quantity.toPlainString()} ${entry.unitName}")
            }

            if (isValidQuantity) {
                currentUnit?.let { unit ->
                    entries.add("$quantityText ${unit.unitName}")
                }
            }

            return entries.joinToString(", ")
        }

    /**
     * Satırın dokümana eklenip eklenemeyeceğini kontrol eder
     */
    val canAddToDocument: Boolean
        get() = hasAnyEntry &&
                validationStatus !is ValidationStatus.Error &&
                currentUnit != null
}

/**
 * Kaydedilmiş birim girişi
 */
data class UnitEntry(
    val unitId: String,
    val unitName: String,
    val quantity: BigDecimal,
    val hasDiscount: Boolean = false,
    val discountSlots: List<DiscountSlotEntry> = emptyList()
)