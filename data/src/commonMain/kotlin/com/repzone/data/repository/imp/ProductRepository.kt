package com.repzone.data.repository.imp

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilters
import com.repzone.domain.repository.IProductRepository

class ProductRepository(private val iDatabaseManager: IDatabaseManager): IProductRepository {
    //region Field
    private var dummyProducts: List<ProductInformationModel>
    //endregion

    //region Properties

    //endregion

    init {
        dummyProducts = generateDummyProducts()
    }

    //region Public Method
    override suspend fun getProducts(page: Int, pageSize: Int, searchQuery: String, brands: Set<String>, categories: Set<String>, colors: Set<String>, tags: Set<String>, priceRange: PriceRange?): List<ProductInformationModel> {
        return dummyProducts
    }

    override suspend fun getAvailableFilters(): ProductFilters {
        return ProductFilters()
    }

    override suspend fun getProductById(productId: Int): ProductInformationModel? {
        return dummyProducts.find { it.id == productId }
    }
    //endregion

    //region Private Method
    private fun generateDummyProducts(): List<ProductInformationModel> {
        val brands = listOf("Apple", "Samsung", "Sony", "LG", "Xiaomi", "Huawei", "Asus", "Dell", "HP", "Lenovo")
        val groups = listOf("Telefon", "Tablet", "Laptop", "Aksesuar", "TV")
        val tagOptions = listOf("Elektronik", "Yeni", "İndirimli", "Popüler", "Öne Çıkan", "Sınırlı Stok", "Kampanya")

        return (1..50).map { index ->
            val brandIndex = index % brands.size
            val groupIndex = (index % 5)
            val brand = brands[brandIndex]
            val selectedTags = tagOptions.shuffled().take((0..3).random())

            ProductInformationModel(
                id = index,
                name = "${brand} Ürün $index",
                sku = "CODE-$index",
                vat = BigDecimal.fromInt(20),
                tags = selectedTags,
                brandId = brandIndex + 1,
                brandName = brand,
                groupId = groupIndex + 1,
                groupName = groups[groupIndex],
                stock = BigDecimal.fromInt((10..500).random()),
                orderStock = BigDecimal.fromInt((0..50).random()),
                vanStock = BigDecimal.fromInt((0..30).random()),
                transitStock = BigDecimal.fromInt((0..20).random()),
                photoPath = "https://picsum.photos/200/200?random=$index",
                defaultUnitMultiplier = BigDecimal.fromInt(1),
                defaultUnitName = "Adet",
                defaultUnitWeight = BigDecimal.parseString("0.5"),
                units = generateProductUnits(index),
                color = listOf("#FF5733", "#33FF57", "#3357FF", "#F3FF33", "#FF33F3").random(),
                brandPhotoPath = "https://picsum.photos/100/100?brand=$brandIndex",
                groupPhotoPath = "https://picsum.photos/100/100?group=$groupIndex",
                displayOrder = index,
                description = "${brand} marka kaliteli ürün. Ürün kodu: CODE-$index",
                pendingStock = BigDecimal.fromInt((0..10).random()),
                reservedStock = BigDecimal.fromInt((0..15).random()),
                showAvailableStock = index % 2 == 0,
                showTransitStock = index % 3 == 0,
                manufacturerId = if (index % 4 == 0) index + 100 else null
            )
        }
    }

    private fun generateProductUnits(productIndex: Int): List<ProductUnit> {
        val basePrice = BigDecimal.fromInt((50..1000).random())
        val vat = BigDecimal.fromInt(20)

        return listOf(
            ProductUnit(
                unitId = 1,
                unitName = "Adet",
                price = basePrice,
                priceIncludesVat = false,
                vat = vat,
                multiplier = BigDecimal.fromInt(1),
                weight = 0.5,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 100,
                orderQuantityFactor = 1,
                isBaseUnit = true,
                barcode = "869${productIndex.toString().padStart(10, '0')}"
            ),
            ProductUnit(
                unitId = 2,
                unitName = "Kutu",
                price = basePrice * BigDecimal.fromInt(12) * BigDecimal.parseString("0.95"),
                priceIncludesVat = false,
                vat = vat,
                multiplier = BigDecimal.fromInt(12),
                weight = 6.0,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 50,
                orderQuantityFactor = 1,
                isBaseUnit = false,
                barcode = "869${productIndex.toString().padStart(10, '0')}1"
            ),
            ProductUnit(
                unitId = 3,
                unitName = "Koli",
                price = basePrice * BigDecimal.fromInt(48) * BigDecimal.parseString("0.90"),
                priceIncludesVat = false,
                vat = vat,
                multiplier = BigDecimal.fromInt(48),
                weight = 24.0,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 20,
                orderQuantityFactor = 1,
                isBaseUnit = false,
                barcode = "869${productIndex.toString().padStart(10, '0')}2"
            )
        )
    }
    //endregion
}