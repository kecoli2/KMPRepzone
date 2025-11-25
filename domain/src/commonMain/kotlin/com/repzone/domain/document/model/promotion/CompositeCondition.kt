package com.repzone.domain.document.model.promotion

import com.repzone.domain.document.model.PromotionContext


/**
 * Kompozit koşul (AND/OR kombinasyonu)
 */
data class CompositeCondition(val conditions: List<Condition>, val operator: LogicalOperator) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        return when (operator) {
            LogicalOperator.AND -> conditions.all { it.evaluate(context) }
            LogicalOperator.OR -> conditions.any { it.evaluate(context) }
        }
    }
}

/**
 * NOT koşulu (tersine çevir)
 */
data class NotCondition(val condition: Condition) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        return !condition.evaluate(context)
    }
}
