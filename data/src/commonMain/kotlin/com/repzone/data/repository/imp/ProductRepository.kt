package com.repzone.data.repository.imp

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.enums.SalesOperationType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.now
import com.repzone.data.mapper.ProductFlatViewEntityDbMapper
import com.repzone.database.ProductFlatViewEntity
import com.repzone.database.SyncProductPricesEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.query
import com.repzone.database.runtime.rawQueryWithMapper
import com.repzone.database.runtime.select
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.product.PriceRange
import com.repzone.domain.model.product.ProductFilters
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IPriceRepository
import com.repzone.domain.repository.IProductRepository
import com.repzone.domain.util.ProductQueryParams

class ProductRepository(private val iDatabaseManager: IDatabaseManager,
                        private val iPriceRepository: IPriceRepository,
                        private val iModuleParameterRepositoryImpl: IMobileModuleParameterRepository,
                        private val iUserSession: IUserSession, private  val mapper: ProductFlatViewEntityDbMapper
): IProductRepository {
    //region Field
    private var dummyProducts: List<ProductInformationModel>
    //endregion

    //region Properties

    //endregion

    init {
        dummyProducts = generateDummyProducts()
    }

    //region Public Method
    override suspend fun getProducts(quertBuilderSql: String,
                                     page: Int, pageSize: Int,
                                     searchQuery: String,
                                     brands: Set<String>, groupName: Set<String>,
                                     colors: Set<String>, tags: Set<String>,
                                     priceRange: PriceRange?,productMap: MutableMap<Int, List<ProductUnit>>): List<ProductInformationModel> {

        val offset = page * pageSize

        val products = iDatabaseManager.getSqlDriver().query<ProductFlatViewEntity>(quertBuilderSql) {
            where {
                if (searchQuery.isNotBlank()) {
                    criteria("ProductName", like = "%$searchQuery%")
                }

                if (brands.isNotEmpty()) {
                    criteria("BrandName", In = brands.toList())
                }

                if (groupName.isNotEmpty()) {
                    criteria("GroupName", In = groupName.toList())
                }

                if (colors.isNotEmpty()) {
                    criteria("Color", In = colors.toList())
                }

                if (tags.isNotEmpty()) {
                    or {
                        for (tag in tags) {
                            criteria("Tags", like = "%$tag%")
                        }
                    }
                }

                if (priceRange != null && priceRange.isValid) {
                    priceRange.min?.let { min ->
                        criteria("Price", greaterThanOrEqual = min)
                    }
                    priceRange.max?.let { max ->
                        criteria("Price", lessThanOrEqual = max)
                    }
                }
            }

            groupBy {
                groupBy("ProductId")
            }

            limit(pageSize)
            offset(offset)
        }.toList()
        return products.map {
            mapper.toDomain(it, productMap[it.ProductId.toInt()] ?: emptyList())
        }
    }

    override suspend fun getAvailableFilters(): ProductFilters {
        return ProductFilters()
    }

    override suspend fun getProductById(productId: Int): ProductInformationModel? {
        return dummyProducts.find { it.id == productId }
    }

    override suspend fun getProductQueryParams(salesOperationType: SalesOperationType,
                                               currentCustomer: SyncCustomerModel,
                                               customerOrgId: Int,
                                               distController: DistributionControllerModel,
                                               mfrId: Int,
                                               notAllowedMfrs: List<Int>?,
                                               selectedPrefOrgId: Int): ProductQueryParams {
        val nowUtc = now()

        val allBasePriceLists = iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
            where {
                criteria("State", equal = 1)
                criteria("Begin", lessThanOrEqual = nowUtc)
                criteria("End", greaterThanOrEqual = nowUtc)
            }
        }.toList()

        val priceListsByBaseOrgId = iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
            where {
                criteria("State", equal = 1)
                or {
                    criteria("OrganizationId", equal = 0)
                    and {
                        criteria("OrganizationId", greaterThan = 0)
                        criteria("OrganizationId", equal = customerOrgId)
                    }
                }
                criteria("Begin", lessThan = nowUtc)
                criteria("End", greaterThan = nowUtc)
            }
        }.toList()

        //TODO BU YOK BAKILACAK
      /*  val priceListIdsByOrganizationTable = iDatabaseManager.getSqlDriver().select<PriceListOrganization> {

        }*/

        val masterList = mutableListOf<SyncProductPricesEntity>()
        //TODO
        /*for (plist in priceListsByBaseOrgId) {
            for (id in priceListIdsByOrganizationTable) {
                if (plist.id == id) {
                    masterList.add(plist)
                    break
                }
            }
        } */

        for (plist in allBasePriceLists) {
            //TODO BURAYA BAKILACAK
            /* val dynamicListsByPriceListId = iDatabaseManager.getSqlDriver().select<MobileSyncPriceListDynamicListOrganizationModel>{
                .filter { it.priceListId == plist.id && it.state == 1L }
                .map { it.dynamicListId }
            */

           /* val dynamicItemList = iDatabaseManager.getSqlDriver().select<DynamicListItemsEntity> {
                where {
                    criteria("EntityId", equal = customerOrgId)
                    criteria("State", equal = 1L)
                    criteria("DynamicListId", inList = dynamicListsByPriceListId)
                }
            }.toList()*/

            /*if (dynamicItemList.isNotEmpty()) {
                masterList.add(plist)
            }*/
        }

        val validRuledPriceListIds = mutableListOf<Int>()
        for (plist in masterList) {
            if (iPriceRepository.checkPriceListOverRules(plist.Id, currentCustomer.id)) {
                validRuledPriceListIds.add(plist.Id.toInt())
            }
        }

        val customerSalesPriceHeaderId =  getPriceHeaderId(distController.customerPriceListId.toLong(), nowUtc)
        val customerGroupSalesPriceHeaderId = getPriceHeaderId(distController.customerGroupPriceListId.toLong(), nowUtc)
        val customerReturnPriceHeaderId = getPriceHeaderId(distController.customerReturnPriceListId.toLong(), nowUtc)
        val customerGroupReturnPriceHeaderId = getPriceHeaderId(distController.customerGroupReturnPriceListId.toLong(), nowUtc)
        val customerReturnDamagedPriceHeaderId = getPriceHeaderId(distController.customerDamagedPriceListId .toLong(), nowUtc)
        val customerGroupReturnDamagedPriceHeaderId = getPriceHeaderId(distController.customerGroupDamagedPriceListId  .toLong(), nowUtc)
        val isCustomerVatExempt = currentCustomer.isVatExempt
        val showAvailableStock = iModuleParameterRepositoryImpl.getOrdersParameters()?.showProductOrderAvailableStock ?: false
        val showTransitStock = iModuleParameterRepositoryImpl.getOrdersParameters()?.showProductOrderTransitStock ?: false
        val salesNotAllowed = notAllowedMfrs?.toList() ?: emptyList()

        return ProductQueryParams(
            salesOperationType = salesOperationType.ordinal,
            organizationId = customerOrgId,
            repDistId = distController.representativeDistributionListId,
            custDistId = distController.customerDistributionListId,
            custGrpMustStockListId = distController.mustStockListId,
            custGrpPriceListId = customerGroupSalesPriceHeaderId,
            custPriceListId = customerSalesPriceHeaderId,
            customerId = currentCustomer.id.toInt(),
            customerGroupId = currentCustomer.groupId!!.toInt(),
            representId = iUserSession.getActiveSession()!!.identity!!.representativeId,
            custReturnPriceListId = customerReturnPriceHeaderId,
            custGrpReturnPriceListId = customerGroupReturnPriceHeaderId,
            custDmgPriceListId = customerReturnDamagedPriceHeaderId,
            custGrpDmgPriceListId = customerGroupReturnDamagedPriceHeaderId,
            isCustomerVatExempt = isCustomerVatExempt,
            repReturnDistId = distController.representativeReturnDistributionListId,
            custReturnDistId = distController.customerReturnDistributionListId,
            returnPriceType = currentCustomer.returnPriceType?.ordinal ?: 0,
            dmgReturnPriceType = currentCustomer.damagedReturnPriceType?.ordinal ?: 0,
            showTransitStock = showTransitStock,
            showAvailableStock = showAvailableStock,
            mfrId = mfrId,
            notAllowedMfrs = salesNotAllowed,
            prefOrgId = selectedPrefOrgId,
            ruledPriceListIds = validRuledPriceListIds
        )
    }

    override suspend fun getProductUnitFlatQuery(sql: String): MutableMap<Int, List<ProductUnit>> {
        val productListUnit = mutableListOf<ProductUnit>()
        iDatabaseManager.getSqlDriver().rawQueryWithMapper(sql){ cursor ->
           productListUnit.add(
               ProductUnit(
                   barcode = cursor.getString(0),
                   multiplier = BigDecimal.fromDouble(cursor.getDouble(1) ?: 1.0),
                   price = BigDecimal.fromDouble(cursor.getDouble(2) ?: 0.0),
                   unitId = cursor.getLong(3)!!.toInt(),
                   unitName = cursor.getString(4) ?: "",
                   weight = cursor.getDouble(5),
                   minimumOrderQuantity = cursor.getLong(6)?.toInt(),
                   maxOrderQuantity = cursor.getLong(7)?.toInt(),
                   orderQuantityFactor = cursor.getLong(8)?.toInt() ?: 0,
                   vat = BigDecimal.fromDouble(cursor.getDouble(9) ?: 0.0),
                   unitDisplayOrder = cursor.getLong(10)?.toInt() ?: 0,
                   productId = cursor.getLong(11)!!,
                   isBaseUnit = false
               )
           )
        }

        return productListUnit
            .groupBy { it.productId.toInt() }
            .mapValues { (_, units) ->
                val minDisplayOrder = units.minOf { it.unitDisplayOrder }
                units.map { unit ->
                    unit.copy(isBaseUnit = unit.unitDisplayOrder == minDisplayOrder)
                }
            }
            .toMutableMap()
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
                barcode = "869${productIndex.toString().padStart(10, '0')}",
                productId = productIndex.toLong(),
                unitDisplayOrder = 1
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
                barcode = "869${productIndex.toString().padStart(10, '0')}1",
                productId = productIndex.toLong(),
                unitDisplayOrder = 1

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
                barcode = "869${productIndex.toString().padStart(10, '0')}2",
                productId = productIndex.toLong(),
                unitDisplayOrder = 1
            )
        )
    }
    private suspend fun getPriceHeaderId(priceListId: Long, nowUtc: Long): Int {
        return iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
            where {
                criteria("State", equal = 1L)
                criteria("Begin", lessThan = nowUtc)
                criteria("End", greaterThan = nowUtc)
                criteria("Id", equal = priceListId)
            }
        }.firstOrNull()?.Id?.toInt() ?: -1
    }
    //endregion
}