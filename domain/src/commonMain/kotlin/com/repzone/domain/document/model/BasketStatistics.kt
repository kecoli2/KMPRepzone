package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class BasketStatistics(
    // Miktar Bilgileri
    val totalBaseQuantity: BigDecimal = BigDecimal.Companion.ZERO,
    val unitBreakdown: List<UnitStatistic> = emptyList(),

    // Finansal Bilgiler
    val grossTotal: BigDecimal = BigDecimal.Companion.ZERO,
    val discountAmount: BigDecimal = BigDecimal.Companion.ZERO,
    val discountPercentage: BigDecimal = BigDecimal.Companion.ZERO,
    val netTotal: BigDecimal = BigDecimal.Companion.ZERO,
    val vatTotal: BigDecimal = BigDecimal.Companion.ZERO,
    val grandTotal: BigDecimal = BigDecimal.Companion.ZERO,

    // Çeşitlilik Bilgileri
    val lineCount: Int = 0,
    val productCount: Int = 0,
    val brandCount: Int = 0,
    val groupCount: Int = 0,

    // Lojistik Bilgileri
    val totalWeight: BigDecimal = BigDecimal.Companion.ZERO
) {
    val isEmpty: Boolean get() = lineCount == 0

    /**
     * İndirim var mı kontrolü
     */
    val hasDiscount: Boolean get() = discountAmount.compare(BigDecimal.Companion.ZERO) > 0

    companion object {
        val EMPTY = BasketStatistics()
    }
}