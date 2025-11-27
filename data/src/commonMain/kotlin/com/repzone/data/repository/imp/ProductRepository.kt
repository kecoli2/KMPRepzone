package com.repzone.data.repository.imp

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilters
import com.repzone.domain.repository.IProductRepository

class ProductRepository(private val iDatabaseManager: IDatabaseManager): IProductRepository {
    //region Field
    private var dummyProducts: List<Product>
    //endregion

    //region Properties

    //endregion

    init {
        dummyProducts = generateDummyProducts()
    }

    //region Public Method
    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
        searchQuery: String,
        brands: Set<String>,
        categories: Set<String>,
        colors: Set<String>,
        tags: Set<String>,
        priceRange: PriceRange?,
    ): List<Product> {
        return dummyProducts
    }

    override suspend fun getAvailableFilters(): ProductFilters {
        return ProductFilters()
    }

    override suspend fun getProductById(productId: String): Product? {
        return dummyProducts.find { it.id == productId }
    }
    //endregion

    //region Private Method
    private fun generateDummyProducts(): List<Product> {
        val brands = listOf("Apple", "Samsung", "Sony", "LG", "Xiaomi", "Huawei", "Asus", "Dell", "HP", "Lenovo")
        val tagOptions = listOf("Elektronik", "Yeni", "İndirimli", "Popüler", "Öne Çıkan", "Sınırlı Stok", "Kampanya")

        return (1..50).map { index ->
            val productId = "PRD-${index.toString().padStart(4, '0')}"
            val brand = brands[index % brands.size]
            val selectedTags = tagOptions.shuffled().take((0..3).random())

            Product(
                id = productId,
                name = "${brand} Ürün $index",
                code = "CODE-$index",
                groupId = "GRP-${(index % 5) + 1}",
                tags = selectedTags,
                stockQuantity = BigDecimal.fromInt((10..500).random()),
                stockUnitId = "UNIT-ADET",
                units = generateProductUnits(productId, index),
                vatRate = BigDecimal.fromInt(20),
                brand = brand
            )
        }
    }

    private fun generateProductUnits(productId: String, index: Int): List<ProductUnit> {
        val basePrice = BigDecimal.fromInt((50..1000).random())

        return listOf(
            ProductUnit(
                id = "${productId}-UNIT-1",
                productId = productId,
                unitId = "UNIT-ADET",
                unitName = "Adet",
                conversionFactor = BigDecimal.fromInt(1),
                isBaseUnit = true,
                price = basePrice,
                barcode = "869${index.toString().padStart(10, '0')}",
                priceIncludesVat = false
            ),
            ProductUnit(
                id = "${productId}-UNIT-2",
                productId = productId,
                unitId = "UNIT-KUTU",
                unitName = "Kutu",
                conversionFactor = BigDecimal.fromInt(12),
                isBaseUnit = false,
                price = basePrice * BigDecimal.fromInt(12) * BigDecimal.parseString("0.95"), // %5 kutu indirimi
                barcode = "869${index.toString().padStart(10, '0')}1",
                priceIncludesVat = false
            ),
            ProductUnit(
                id = "${productId}-UNIT-3",
                productId = productId,
                unitId = "UNIT-KOLI",
                unitName = "Koli",
                conversionFactor = BigDecimal.fromInt(48),
                isBaseUnit = false,
                price = basePrice * BigDecimal.fromInt(48) * BigDecimal.parseString("0.90"), // %10 koli indirimi
                barcode = "869${index.toString().padStart(10, '0')}2",
                priceIncludesVat = false
            )
        )
    }
    //endregion
}