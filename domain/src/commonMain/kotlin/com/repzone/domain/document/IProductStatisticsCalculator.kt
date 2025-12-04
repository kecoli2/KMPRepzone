package com.repzone.domain.document

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.BasketStatistics
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductStatistics
import com.repzone.domain.model.product.ProductRowState

interface IProductStatisticsCalculator {
    suspend fun calculate(products: Map<Int, ProductInformationModel>, rowStates: Map<Int, ProductRowState>): ProductStatistics
    suspend fun calculateBasketStatistics(lines: List<IDocumentLine>, invoiceDiscounts: List<BigDecimal> = emptyList()): BasketStatistics
}