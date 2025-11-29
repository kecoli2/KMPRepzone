package com.repzone.domain.repository

import com.repzone.core.enums.SalesOperationType
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilters
import com.repzone.domain.util.ProductQueryBuilder
import com.repzone.domain.util.ProductQueryParams

interface IProductRepository {

    suspend fun getProducts(
        quertBuilderSql: String,page: Int,
        pageSize: Int,
        searchQuery: String = "",
        brands: Set<String> = emptySet(),
        groupName: Set<String> = emptySet(),
        colors: Set<String> = emptySet(),
        tags: Set<String> = emptySet(),
        priceRange: PriceRange? = null,
        productMap: MutableMap<Int, List<ProductUnit>> = mutableMapOf()
    ): List<ProductInformationModel>

    /**
     * Get distinct filter values from all products
     */
    suspend fun getAvailableFilters(params: ProductQueryParams): ProductFilters

    suspend fun getProductById(quertBuilderSql: String, productId: Int, unitList: List<ProductUnit>): ProductInformationModel
    suspend fun getProductQueryParams(salesOperationType: SalesOperationType,
                                      currentCustomer: SyncCustomerModel,
                                      customerOrgId: Int,
                                      distController: DistributionControllerModel,
                                      mfrId: Int = 0,
                                      notAllowedMfrs: List<Int>? = null,
                                      selectedPrefOrgId: Int = 0): ProductQueryParams
    suspend fun getProductUnitFlatQuery(sql: String): MutableMap<Int, List<ProductUnit>>
}