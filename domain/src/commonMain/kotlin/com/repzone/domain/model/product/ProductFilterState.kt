package com.repzone.domain.model.product

/**
 * Ürün listesi filter state
 */
data class ProductFilterState(
    val searchQuery: String = "",
    val brands: Set<String> = emptySet(),
    val categories: Set<String> = emptySet(),
    val colors: Set<String> = emptySet(),
    val priceRange: PriceRange? = null,
    val tags: Set<String> = emptySet()
) {
    /**
     * Actif filter var mı buraya daha sonra eklemeler yapılabilir
     */
    val hasActiveFilters: Boolean
        get() = searchQuery.isNotEmpty() ||
                brands.isNotEmpty() ||
                categories.isNotEmpty() ||
                colors.isNotEmpty() ||
                priceRange != null ||
                tags.isNotEmpty()

    /**
     * Şuanda aktif olan filtreler
     */
    val activeFilterCount: Int
        get() = listOf(
            brands.isNotEmpty(),
            categories.isNotEmpty(),
            colors.isNotEmpty(),
            priceRange != null,
            tags.isNotEmpty()
        ).count { it }
}