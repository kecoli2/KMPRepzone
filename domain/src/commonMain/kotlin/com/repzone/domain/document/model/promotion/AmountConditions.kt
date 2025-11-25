package com.repzone.domain.document.model.promotion

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.model.PromotionContext


/**
 * Miktar koşulu
 */
data class QuantityCondition(val minQuantity: BigDecimal? = null, val maxQuantity: BigDecimal? = null) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val qty = context.currentLine?.quantity ?: return false
        val minOk = minQuantity?.let { qty >= it } ?: true
        val maxOk = maxQuantity?.let { qty <= it } ?: true
        return minOk && maxOk
    }
}

/**
 * Satır tutar koşulu
 */
data class LineTotalCondition(val minAmount: BigDecimal? = null, val maxAmount: BigDecimal? = null) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val total = context.currentLine?.lineTotal ?: return false
        val minOk = minAmount?.let { total >= it } ?: true
        val maxOk = maxAmount?.let { total <= it } ?: true
        return minOk && maxOk
    }
}

/**
 * Belge tutar koşulu
 */
data class DocumentTotalCondition(val minAmount: BigDecimal? = null, val maxAmount: BigDecimal? = null) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val total = context.documentTotal
        val minOk = minAmount?.let { total >= it } ?: true
        val maxOk = maxAmount?.let { total <= it } ?: true
        return minOk && maxOk
    }
}
