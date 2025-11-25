package com.repzone.domain.document

import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.AppliedDiscount
import com.repzone.domain.document.model.GiftEvaluation
import com.repzone.domain.document.model.LineDiscountEvaluation
import com.repzone.domain.document.model.PromotionContext
import com.repzone.domain.document.model.PromotionResult

interface IPromotionEngine {
    /**
     * Tüm promosyonları değerlendirir
     */
    suspend fun evaluate(context: PromotionContext): PromotionResult

    /**
     * Sadece satır iskontolarını değerlendirir
     */
    suspend fun evaluateLineDiscounts(lines: List<IDocumentLine>, context: PromotionContext): List<LineDiscountEvaluation>

    /**
     * Belge iskontolarını değerlendirir
     */
    suspend fun evaluateDocumentDiscounts(context: PromotionContext): List<AppliedDiscount>

    /**
     * Hediye kurallarını değerlendirir
     */
    suspend fun evaluateGifts(context: PromotionContext): GiftEvaluation
}