package com.repzone.domain.document.model.promotion

import com.repzone.domain.document.model.PromotionContext


/**
 * Promosyon kuralı base interface
 */
interface PromotionRule {
    val id: String
    val name: String
    val priority: Int
    val conditions: List<Condition>
    val isActive: Boolean
    
    /**
     * Kuralın uygulanabilir olup olmadığını kontrol eder
     */
    fun isApplicable(context: PromotionContext): Boolean {
        if (!isActive) return false
        return conditions.all { it.evaluate(context) }
    }
}

/**
 * İskonto scope'u
 */
enum class DiscountScope {
    /** Satır bazlı (tek satıra uygulanır) */
    LINE,
    
    /** Belge bazlı (tüm satırlara uygulanır) */
    DOCUMENT
}
