package com.repzone.domain.document.model.promotion

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.model.ConflictResolution
import com.repzone.domain.document.model.DiscountCalculationMode
import com.repzone.domain.document.model.DiscountType

/**
 * İskonto kuralı
 */
data class DiscountRule(
    override val id: String,
    override val name: String,
    override val priority: Int,
    override val conditions: List<Condition>,
    override val isActive: Boolean = true,

    /** İskonto scope'u */
    val scope: DiscountScope,

    /** Hedef slot (1-8) */
    val targetSlot: Int,

    /** İskonto tipi */
    val discountType: DiscountType,

    /** İskonto değeri */
    val value: BigDecimal,

    /** Hesaplama modu (null = genel ayar) */
    val calculationMode: DiscountCalculationMode? = null,

    /** Conflict çözüm stratejisi */
    val conflictResolution: ConflictResolution = ConflictResolution.ASK_USER
) : PromotionRule {
    
    /**
     * Efektif değer (karşılaştırma için)
     */
    fun effectiveValue(): BigDecimal = value
}
