@file:OptIn(ExperimentalTime::class)

package com.repzone.domain.document.model.promotion

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.domain.document.model.PromotionContext
import kotlin.time.ExperimentalTime


/**
 * Satır ürün koşulu (mevcut satır için)
 */
data class ProductCondition(val productIds: List<String>, val minQuantity: BigDecimal = BigDecimal.ONE) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val line = context.currentLine ?: return false
        return line.productId in productIds && line.quantity >= minQuantity
    }
}

/**
 * Satır ürün grubu koşulu (mevcut satır için)
 */
data class ProductGroupCondition(val groupIds: List<String>, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val line = context.currentLine ?: return false
        // ProductGroupId DocumentLine'a eklenmeli veya Product'tan alınmalı
        // Şimdilik basitleştirilmiş versiyon
        return false // TODO: Product groupId'si DocumentLine'da olmalı
    }
}

/**
 * Belgede ürün var mı koşulu
 */
data class HasProductCondition(val productIds: List<String>, val minQuantity: BigDecimal = BigDecimal.ONE, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val matchingLines = context.allLines.filter { it.productId in productIds }
        
        return when (matchType) {
            MatchType.ANY -> {
                matchingLines.any { it.quantity >= minQuantity }
            }
            MatchType.ALL -> {
                productIds.all { productId ->
                    matchingLines.find { it.productId == productId }
                        ?.let { it.quantity >= minQuantity } ?: false
                }
            }
            MatchType.NONE -> {
                matchingLines.none { it.quantity >= minQuantity }
            }
        }
    }
}

/**
 * Belgede ürün grubu var mı koşulu
 */
data class HasProductGroupCondition(val groupIds: List<String>, val minQuantity: BigDecimal = BigDecimal.ONE, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        // TODO: Product groupId'si gerekli
        return false
    }
}

/**
 * Müşteri geçmişte ürün aldı mı koşulu
 */
data class CustomerPurchasedProductCondition(val productIds: List<String>, val minQuantity: BigDecimal = BigDecimal.ONE, val withinDays: Int? = null, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val customer = context.customer ?: return false
        val purchaseHistory = context.customerPurchaseHistory ?: return false

        val cutoffDate = withinDays?.let {
            now().toInstant().minus(
                kotlin.time.Duration.parse("${it}d")
            )
        }

        val relevantPurchases = purchaseHistory
            .filter { purchase ->
                purchase.productId in productIds &&
                        (cutoffDate == null || purchase.date >= cutoffDate)
            }

        return when (matchType) {
            MatchType.ANY -> {
                relevantPurchases.fold(BigDecimal.ZERO) { acc, purchase -> acc + purchase.quantity } >= minQuantity
            }

            MatchType.ALL -> {
                productIds.all { productId ->
                    relevantPurchases
                        .filter { it.productId == productId }
                        .fold(BigDecimal.ZERO) { acc, purchase -> acc + purchase.quantity } >= minQuantity
                }
            }

            MatchType.NONE -> {
                relevantPurchases.isEmpty()
            }
        }
    }
}

/**
 * Müşteri geçmişte ürün grubu aldı mı koşulu
 */
data class CustomerPurchasedGroupCondition(val groupIds: List<String>, val minQuantity: BigDecimal = BigDecimal.ONE, val withinDays: Int? = null, val matchType: MatchType = MatchType.ANY) : Condition {
    override fun evaluate(context: PromotionContext): Boolean {
        val purchaseHistory = context.customerPurchaseHistory ?: return false
        
        val cutoffDate = withinDays?.let {
            now().toInstant().minus(
                kotlin.time.Duration.parse("${it}d")
            )
        }
        
        val relevantPurchases = purchaseHistory
            .filter { purchase ->
                purchase.productGroupId in groupIds &&
                (cutoffDate == null || purchase.date >= cutoffDate)
            }
        
        return when (matchType) {
            MatchType.ANY -> {
                relevantPurchases.fold(BigDecimal.ZERO){ acc, purchase -> acc + purchase.quantity } >= minQuantity
            }
            MatchType.ALL -> {
                groupIds.all { groupId ->
                    relevantPurchases
                        .filter { it.productGroupId == groupId }
                        .fold(BigDecimal.ZERO){ acc, purchase -> acc + purchase.quantity } >= minQuantity
                }
            }
            MatchType.NONE -> {
                relevantPurchases.isEmpty()
            }
        }
    }
}
