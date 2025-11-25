package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * İskonto hesaplama sonucu
 */
data class DiscountCalculationResult(
    /** Başlangıç fiyatı */
    val basePrice: BigDecimal,

    /** Hesaplama adımları */
    val steps: List<DiscountStep>,

    /** Toplam iskonto tutarı */
    val totalDiscount: BigDecimal,

    /** Net fiyat */
    val netPrice: BigDecimal
) {
    /**
     * Efektif iskonto yüzdesi
     */
    val effectiveDiscountPercentage: BigDecimal
        get() = if (basePrice > BigDecimal.ZERO) {
            (totalDiscount / basePrice) * 100
        } else BigDecimal.ZERO
}

/**
 * İskonto hesaplama adımı
 */
data class DiscountStep(
    /** Slot numarası */
    val slotNumber: Int,
    
    /** Açıklama */
    val description: String,
    
    /** Referans fiyat (hangi tutar üzerinden hesaplandı) */
    val referencePrice: BigDecimal,
    
    /** İskonto değeri (%10 veya 50₺) */
    val discountValue: BigDecimal,
    
    /** İskonto tipi */
    val discountType: DiscountType,
    
    /** Gerçek düşülen tutar */
    val discountAmount: BigDecimal,
    
    /** Bu adımdan sonraki net */
    val netAfter: BigDecimal,
    
    /** Hesaplama modu */
    val mode: DiscountCalculationMode
)
