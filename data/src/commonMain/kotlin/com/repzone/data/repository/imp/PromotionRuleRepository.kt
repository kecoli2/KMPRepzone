package com.repzone.data.repository.imp

import com.repzone.domain.document.model.promotion.DiscountRule
import com.repzone.domain.document.model.promotion.GiftRule
import com.repzone.domain.document.model.promotion.PromotionRule
import com.repzone.domain.repository.IPromotionRuleRepository

class PromotionRuleRepository: IPromotionRuleRepository {
    //region Field
    //endregion

    //region Public Method
    override suspend fun getActiveRules(): List<PromotionRule> {
        return emptyList()
    }

    override suspend fun getActiveDiscountRules(): List<DiscountRule> {
        return emptyList()
    }

    override suspend fun getActiveGiftRules(): List<GiftRule> {
        return emptyList()
    }

    override suspend fun getRuleById(id: String): PromotionRule? {
        return null
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}