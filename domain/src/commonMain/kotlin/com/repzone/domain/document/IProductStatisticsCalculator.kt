package com.repzone.domain.document

import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductStatistics
import com.repzone.domain.model.product.ProductRowState

interface IProductStatisticsCalculator {
    suspend fun calculate(products: Map<Int, ProductInformationModel>, rowStates: Map<Int, ProductRowState>): ProductStatistics
}