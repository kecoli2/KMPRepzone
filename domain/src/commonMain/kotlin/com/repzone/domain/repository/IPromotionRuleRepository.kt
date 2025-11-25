package com.repzone.domain.repository

import com.repzone.domain.document.model.promotion.DiscountRule
import com.repzone.domain.document.model.promotion.GiftRule
import com.repzone.domain.document.model.promotion.PromotionRule

interface IPromotionRuleRepository {
    /**
     * Aktif tüm kuralları getirir
     */
    suspend fun getActiveRules(): List<PromotionRule>

    /**
     * Aktif iskonto kurallarını getirir
     */
    suspend fun getActiveDiscountRules(): List<DiscountRule>

    /**
     * Aktif hediye kurallarını getirir
     */
    suspend fun getActiveGiftRules(): List<GiftRule>

    /**
     * ID ile kural getirir
     */
    suspend fun getRuleById(id: String): PromotionRule?
}