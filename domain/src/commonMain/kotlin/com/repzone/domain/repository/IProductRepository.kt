package com.repzone.domain.repository

import com.repzone.domain.document.model.Product
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilters

interface IProductRepository {

    suspend fun getProducts(page: Int,
        pageSize: Int,
        searchQuery: String = "",
        brands: Set<String> = emptySet(),
        categories: Set<String> = emptySet(),
        colors: Set<String> = emptySet(),
        tags: Set<String> = emptySet(),
        priceRange: PriceRange? = null
    ): List<Product>

    /**
     * Get distinct filter values from all products
     */
    suspend fun getAvailableFilters(): ProductFilters
}