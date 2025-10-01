package com.repzone.sync.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.model.SyncPage
import io.ktor.client.HttpClient
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SyncApiProductImpl(private val client: HttpClient): ISyncApiService<MobileProductDto> {
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

    override suspend fun fetchPage(model: SyncModuleModel, page: Int, size: Int): ApiResult<SyncPage<MobileProductDto>> {
        TODO("Not yet implemented")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    @OptIn(ExperimentalTime::class)
    private fun generate100KProducts(startId: Long = 1): List<MobileProductDto> {
        return List(20_000) { index ->
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
                modificationDateUtc = Clock.System.now().toEpochMilliseconds() - Random.nextLong(0, 31536000000),
                name = "Product $uniqueId",
                orderQuantityFactor = Random.nextLong(1, 10).toInt(),
                organizationId = Random.nextLong(1, 50).toInt(),
                organizationIds = "${Random.nextInt(1, 50)},${Random.nextInt(1, 50)}",
                photoPath = "/photos/products/$uniqueId.jpg",
                sku = "SKU-${uniqueId.toString().padStart(6, '0')}",
                state = Random.nextLong(0, 3).toInt(),
                tags = listOf("new", "sale", "featured", "popular").shuffled().take(2).joinToString(","),
                vat = 0.toDouble(),
                units = emptyList(),
                parameters = emptyList()
            )
        }
    }
    //endregion
}