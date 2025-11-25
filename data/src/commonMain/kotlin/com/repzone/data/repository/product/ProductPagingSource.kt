package com.repzone.data.repository.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.repzone.domain.document.model.Product
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.repository.IProductRepository

/**
 * Filtreleme desteği olan ürün listesi için PagingSource.
 * 20K+ ürün ile verimli şekilde çalışır.
 */
class ProductPagingSource(
    private val productRepository: IProductRepository,
    private val filterState: ProductFilterState
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            // Filtrelerle ürünleri al
            val products = productRepository.getProducts(
                page = page,
                pageSize = pageSize,
                searchQuery = filterState.searchQuery,
                brands = filterState.brands,
                categories = filterState.categories,
                colors = filterState.colors,
                tags = filterState.tags,
                priceRange = filterState.priceRange
            )

            LoadResult.Page(
                data = products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (products.isEmpty() || products.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}