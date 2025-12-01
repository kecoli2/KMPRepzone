package com.repzone.domain.document.base

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.model.DiscountSlot
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit

interface IDocumentLine {
    val id: String
    val productId: Int
    val productName: String
    val unitId: Int
    val unitName: String
    val conversionFactor: BigDecimal
    val quantity: BigDecimal
    val unitPrice: BigDecimal
    val vatRate: BigDecimal

    // Computed properties
    val vatAmount: BigDecimal
    val grossUnitPrice: BigDecimal
    val lineTotalVat: BigDecimal
    val lineTotalGross: BigDecimal

    // Discount slots (1-8)
    val discount1: DiscountSlot
    val discount2: DiscountSlot
    val discount3: DiscountSlot
    val discount4: DiscountSlot
    val discount5: DiscountSlot
    val discount6: DiscountSlot
    val discount7: DiscountSlot
    val discount8: DiscountSlot

    // Computed properties
    val discountSlots: List<DiscountSlot>
    val baseQuantity: BigDecimal
    val netUnitPrice: BigDecimal
    val lineTotal: BigDecimal
    val productUnit: ProductUnit
    val productInfo: ProductInformationModel

    /**
     * Belirli bir slot'u günceller
     * @param slotNumber 1-8 arası slot numarası
     * @param slot Yeni iskonto slot değeri
     * @return Güncellenmiş satır
     */
    fun withSlot(slotNumber: Int, slot: DiscountSlot): IDocumentLine

    /**
     * Belirli bir slot'u getirir
     * @param slotNumber 1-8 arası slot numarası
     * @return İskonto slot değeri
     */
    fun getSlot(slotNumber: Int): DiscountSlot

    fun hasDiscount(): Boolean

    fun updateQuantity(value: BigDecimal, unit: ProductUnit): IDocumentLine
}