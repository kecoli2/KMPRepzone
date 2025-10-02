package com.repzone.sync.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.toModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.dto.ServiceProductUnitDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.ISyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SyncApiProductImpl(private val client: HttpClient): ISyncApiService<List<MobileProductDto>> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun fetchAll(model: SyncModuleModel): ApiResult<List<MobileProductDto>> {
        return try{
           /* val response = client.post(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
            }
            ApiResult.Success(response.body<List<SyncProductModel>>())*/
            ApiResult.Success(generate100KProducts())
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }

    override suspend fun fetchPage(model: SyncModuleModel, pageSize: Int): Flow<ApiResult<List<MobileProductDto>>> = flow {
        val requestModel = model.requestFilter?.toModel<FilterModelRequest>()
        var currentPAge = 0
        var hasMore = true
        if(model.lastSyncDate == null){
            requestModel?.fetchOnlyActive = true
        }else{
            requestModel?.lastModDate = model.lastSyncDate?.toDateString("yyyy-MM-dd HH:mm:ss.fff")
        }

        while (hasMore){
            try {

                val response = client.safePost<List<MobileProductDto>>(model.requestUrl!!){
                    setBody(requestModel)
                }
                when(response){
                    is ApiResult.Error -> {
                        throw Exception("API Error: ${response.exception.message}")
                    }
                    is ApiResult.Loading -> {

                    }
                    is ApiResult.Success -> {
                        val data = response.data
                        if(data.isEmpty()){
                            hasMore = false
                        }else{
                            emit(ApiResult.Success(data))
                            currentPAge++
                            hasMore = data.size >= pageSize
                            requestModel?.lastId = data.last().id
                        }
                    }
                }
            }catch (ex: Exception){
                emit(ApiResult.Error(ex.toApiException()))
                hasMore = false
            }
        }

    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    @OptIn(ExperimentalTime::class)
    private fun generate100KProducts(startId: Long = 1): List<MobileProductDto> {
        return List(20_0) { index ->
            val uniqueId = startId + index
            MobileProductDto(
                id = uniqueId.toInt(),
                brandId = Random.nextLong(1, 1000).toInt(),
                brandName = "Brand ${Random.nextInt(1, 1000)}",
                brandPhotoPath = "/photos/brands/${Random.nextInt(1, 1000)}.jpg",
                closeToReturns = false,
                closeToSales = false,
                color = listOf("Red", "Blue", "Green", "Yellow", "Black", "White").random(),
                description = "Product description $uniqueId",
                displayOrder = Random.nextLong(0, 10000).toInt(),
                groupId = Random.nextLong(1, 100).toInt(),
                groupName = "Group ${Random.nextInt(1, 100)}",
                groupPhotoPath = "/photos/groups/${Random.nextInt(1, 100)}.jpg",
                maximumOrderQuantity = Random.nextLong(10, 1000).toInt(),
                minimumOrderQuantity = Random.nextLong(1, 10).toInt(),
                modificationDateUtc = Clock.System.now(),
                name = "Product $uniqueId",
                orderQuantityFactor = Random.nextLong(1, 10).toInt(),
                organizationId = Random.nextLong(1, 50).toInt(),
                organizationIds = "${Random.nextInt(1, 50)},${Random.nextInt(1, 50)}",
                photoPath = "/photos/products/$uniqueId.jpg",
                sku = "SKU-${uniqueId.toString().padStart(6, '0')}",
                state = Random.nextLong(0, 3).toInt(),
                tags = listOf("new", "sale", "featured", "popular").shuffled().take(2).joinToString(","),
                vat = 0.toDouble(),
                units = generateUnity(uniqueId),
                parameters = emptyList()
            )
        }
    }

    private fun generateUnity(productID: Long): List<ServiceProductUnitDto>{
        return listOf(
            ServiceProductUnitDto(
                id = 1,
                state = 1,
                unitId = 101,
                multiplier = 1,
                barcode = "1234567890123",
                weight = 0.5,
                displayOrder = 1,
                selected = true,
                priceId = 201,
                price = 29.99,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 100,
                orderQuantityFactor = 1,
                salesReturnPrice = 25.99,
                salesDamagedReturnPrice = 20.99,
                purchasePrice = 15.99,
                purchaseReturnPrice = 14.99,
                purchaseDamagedReturnPrice = 12.99
            ),
            ServiceProductUnitDto(
                id = 2,
                state = 1,
                unitId = 102,
                multiplier = 12,
                barcode = "9876543210987",
                weight = 6.0,
                displayOrder = 2,
                selected = false,
                priceId = 202,
                price = 349.99,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 50,
                orderQuantityFactor = 12,
                salesReturnPrice = 329.99,
                salesDamagedReturnPrice = 299.99,
                purchasePrice = 189.99,
                purchaseReturnPrice = 179.99,
                purchaseDamagedReturnPrice = 159.99
            ),
            ServiceProductUnitDto(
                id = 3,
                state = 1,
                unitId = 103,
                multiplier = 6,
                barcode = "5551234567890",
                weight = 3.2,
                displayOrder = 3,
                selected = true,
                priceId = 203,
                price = 179.50,
                minimumOrderQuantity = 6,
                maxOrderQuantity = 120,
                orderQuantityFactor = 6,
                salesReturnPrice = 169.50,
                salesDamagedReturnPrice = 149.50,
                purchasePrice = 95.00,
                purchaseReturnPrice = 89.00,
                purchaseDamagedReturnPrice = 79.00
            ),
            ServiceProductUnitDto(
                id = 4,
                state = 0,
                unitId = 104,
                multiplier = 24,
                barcode = "7778889990001",
                weight = null,
                displayOrder = 4,
                selected = false,
                priceId = 204,
                price = 599.99,
                minimumOrderQuantity = 24,
                maxOrderQuantity = 240,
                orderQuantityFactor = 24,
                salesReturnPrice = null,
                salesDamagedReturnPrice = null,
                purchasePrice = null,
                purchaseReturnPrice = null,
                purchaseDamagedReturnPrice = null
            ),
            ServiceProductUnitDto(
                id = 5,
                state = 1,
                unitId = 105,
                multiplier = 1,
                barcode = "4445556667778",
                weight = 1.25,
                displayOrder = 5,
                selected = true,
                priceId = 205,
                price = 89.99,
                minimumOrderQuantity = 1,
                maxOrderQuantity = 200,
                orderQuantityFactor = 1,
                salesReturnPrice = 79.99,
                salesDamagedReturnPrice = 69.99,
                purchasePrice = 45.00,
                purchaseReturnPrice = 42.00,
                purchaseDamagedReturnPrice = 38.00
            )
        )
    }
    //endregion
}