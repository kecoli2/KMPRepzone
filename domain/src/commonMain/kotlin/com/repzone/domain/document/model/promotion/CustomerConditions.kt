package com.repzone.domain.document.model.promotion

import com.repzone.domain.document.model.PromotionContext


/**
 * Müşteri grubu koşulu
 */
data class CustomerGroupCondition(val groupIds: List<String>, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val customerGroup = context.customer?.customerGroupName ?: return false
        return when (matchType) {
            MatchType.ANY -> customerGroup in groupIds
            MatchType.ALL -> groupIds.all { it == customerGroup }
            MatchType.NONE -> customerGroup !in groupIds
        }
    }
}

/**
 * Müşteri ek grup koşulu
 */
/*data class CustomerExtraGroupCondition(val extraGroupIds: List<String>, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val extraGroups = context.customer?.customerGroupName ?: return false
        return when (matchType) {
            MatchType.ANY -> extraGroups.any { it in extraGroupIds }
            MatchType.ALL -> extraGroupIds.all { it in extraGroups }
            MatchType.NONE -> extraGroups.none { it in extraGroupIds }
        }
    }
}*/

/**
 * Müşteri tag koşulu
 */
data class CustomerTagCondition(val tags: List<String>, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val customerTags = context.customer?.tagRaw ?: return false
        return when (matchType) {
            MatchType.ANY -> customerTags.any { it in tags }
            MatchType.ALL -> tags.all { it in customerTags }
            MatchType.NONE -> customerTags.none { it in tags }
        }
    }
}
