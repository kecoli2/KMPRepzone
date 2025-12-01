package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.model.ProductFlatModel


/**
 * Belge satırı implementation
 * Pure business logic, no infrastructure dependencies
 */
data class DocumentLine(
    override val id: String,
    override val productId: Int,
    override val productName: String,
    override val unitId: Int,
    override val unitName: String,
    override val conversionFactor: BigDecimal,
    override val quantity: BigDecimal,
    override val unitPrice: BigDecimal,
    override val discount1: DiscountSlot = DiscountSlot.Empty,
    override val discount2: DiscountSlot = DiscountSlot.Empty,
    override val discount3: DiscountSlot = DiscountSlot.Empty,
    override val discount4: DiscountSlot = DiscountSlot.Empty,
    override val discount5: DiscountSlot = DiscountSlot.Empty,
    override val discount6: DiscountSlot = DiscountSlot.Empty,
    override val discount7: DiscountSlot = DiscountSlot.Empty,
    override val discount8: DiscountSlot = DiscountSlot.Empty,
    override val vatRate: BigDecimal,
    override val productUnit: ProductUnit,
    override val productInfo: ProductInformationModel
) : IDocumentLine {

    // Computed properties

    override val vatAmount: BigDecimal
        get() = netUnitPrice * vatRate / 100

    override val grossUnitPrice: BigDecimal  // KDV dahil
        get() = netUnitPrice + vatAmount

    override val lineTotalVat: BigDecimal  // KDV tutarı
        get() = vatAmount * quantity

    override val lineTotalGross: BigDecimal  // KDV dahil satır toplamı
        get() = lineTotal + lineTotalVat
    override val discountSlots: List<DiscountSlot>
        get() = listOf(
            discount1, discount2, discount3, discount4,
            discount5, discount6, discount7, discount8
        )
    
    override val baseQuantity: BigDecimal
        get() = quantity * conversionFactor
    
    override val netUnitPrice: BigDecimal by lazy {
        calculateNetPrice()
    }
    
    override val lineTotal: BigDecimal
        get() = netUnitPrice * quantity
    
    override fun withSlot(slotNumber: Int, slot: DiscountSlot): IDocumentLine {
        return when (slotNumber) {
            1 -> copy(discount1 = slot)
            2 -> copy(discount2 = slot)
            3 -> copy(discount3 = slot)
            4 -> copy(discount4 = slot)
            5 -> copy(discount5 = slot)
            6 -> copy(discount6 = slot)
            7 -> copy(discount7 = slot)
            8 -> copy(discount8 = slot)
            else -> this
        }
    }
    
    override fun getSlot(slotNumber: Int): DiscountSlot {
        return when (slotNumber) {
            1 -> discount1
            2 -> discount2
            3 -> discount3
            4 -> discount4
            5 -> discount5
            6 -> discount6
            7 -> discount7
            8 -> discount8
            else -> DiscountSlot.Empty
        }
    }

    override fun hasDiscount(): Boolean {
        return discountSlots.any { it is DiscountSlot.Applied }
    }

    override fun updateQuantity(value: BigDecimal, unit: ProductUnit): IDocumentLine {
        if(unitId != unit.unitId){
            clearAllDiscounts()
        }
        val newLine = this.copy(
            quantity = value,
            unitId = unit.unitId,
            unitName = unit.unitName,
            conversionFactor = unit.multiplier,
            unitPrice = unit.price,
            productUnit = unit
        )
        calculateNetPrice()
        return newLine
    }

    /**
     * Net fiyat hesaplama
     * Her iskonto slot'u sırayla işlenir
     */
    private fun calculateNetPrice(): BigDecimal {
        var net = unitPrice
        
        discountSlots.forEach { slot ->
            if (slot is DiscountSlot.Applied) {
                val discountAmount = when (slot.type) {
                    DiscountType.PERCENTAGE -> {
                        net * slot.value / 100
                    }
                    DiscountType.FIXED_AMOUNT -> {
                        slot.value
                    }
                }
                net -= discountAmount
            }
        }
        
        return net.coerceAtLeast(BigDecimal.ZERO)
    }

    private fun clearAllDiscounts(){
        this.copy(
            discount1 = DiscountSlot.Empty,
            discount2 = DiscountSlot.Empty,
            discount3 = DiscountSlot.Empty,
            discount4 = DiscountSlot.Empty,
            discount5 = DiscountSlot.Empty,
            discount6 = DiscountSlot.Empty,
            discount7 = DiscountSlot.Empty,
            discount8 = DiscountSlot.Empty
        )
    }
}
