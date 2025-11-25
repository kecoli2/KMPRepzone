package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal


/**
 * İskonto slot'u
 * Bir satırda 8 adet slot bulunur (discount1..discount8)
 */
sealed interface DiscountSlot {
    /**
     * Boş slot - iskonto yok
     */
    object Empty : DiscountSlot
    
    /**
     * Uygulanmış iskonto
     */
    data class Applied(
        /** Hangi kuraldan geldi (null = manuel giriş) */
        val ruleId: String?,

        /** İskonto tipi */
        val type: DiscountType,

        /** İskonto değeri (%10 veya 50₺) */
        val value: BigDecimal,

        /** Açıklama */
        val description: String,

        /** Hesaplama modu (null = genel ayar kullan) */
        val calculationMode: DiscountCalculationMode?,

        /** Manuel mi otomatik mi */
        val isManual: Boolean = false
    ) : DiscountSlot
}

/**
 * İskonto tipi
 */
enum class DiscountType {
    /** Yüzde bazlı (%10) */
    PERCENTAGE,
    
    /** Sabit tutar (50₺) */
    FIXED_AMOUNT
}

/**
 * İskonto hesaplama modu
 */
enum class DiscountCalculationMode {
    /** Düşerek - her iskonto önceki net üzerinden hesaplanır */
    CASCADING,
    
    /** Sabit taban - her iskonto orijinal fiyat üzerinden hesaplanır */
    BASE
}
