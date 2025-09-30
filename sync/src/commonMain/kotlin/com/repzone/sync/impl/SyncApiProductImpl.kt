package com.repzone.sync.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.domain.model.SyncProductModel
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.model.SyncPage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SyncApiProductImpl(private val client: HttpClient): ISyncApiService<SyncProductModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun fetchAll(): ApiResult<List<SyncProductModel>> {
        return try{
           /* val response = client.post(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
            }
            ApiResult.Success(response.body<List<SyncProductModel>>())*/
            ApiResult.Success(generate100KProducts())
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }

    override suspend fun fetchUpdatedSince(sinceIso: String): ApiResult<List<SyncProductModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPage(page: Int, size: Int): ApiResult<SyncPage<SyncProductModel>> {
        TODO("Not yet implemented")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    @OptIn(ExperimentalTime::class)
    private fun generate100KProducts(startId: Long = 1): List<SyncProductModel> {
        return List(20_000) { index ->
            val uniqueId = startId + index
            SyncProductModel(
                Id = uniqueId,
                BrandId = Random.nextLong(1, 1000),
                BrandName = "Brand ${Random.nextInt(1, 1000)}",
                BrandPhotoPath = "/photos/brands/${Random.nextInt(1, 1000)}.jpg",
                CloseToReturns = Random.nextLong(0, 100),
                CloseToSales = Random.nextLong(0, 1000),
                Color = listOf("Red", "Blue", "Green", "Yellow", "Black", "White").random(),
                Description = "Product description $uniqueId",
                DisplayOrder = Random.nextLong(0, 10000),
                GroupId = Random.nextLong(1, 100),
                GroupName = "Group ${Random.nextInt(1, 100)}",
                GroupPhotoPath = "/photos/groups/${Random.nextInt(1, 100)}.jpg",
                IsVisible = Random.nextLong(0, 2),
                ManufacturerId = Random.nextLong(1, 500),
                MaximumOrderQuantity = Random.nextLong(10, 1000),
                MinimumOrderQuantity = Random.nextLong(1, 10),
                ModificationDateUtc = Clock.System.now().toEpochMilliseconds() - Random.nextLong(0, 31536000000),
                Name = "Product $uniqueId",
                OrderQuantityFactor = Random.nextLong(1, 10),
                OrganizationId = Random.nextLong(1, 50),
                OrganizationIds = "${Random.nextInt(1, 50)},${Random.nextInt(1, 50)}",
                PhotoPath = "/photos/products/$uniqueId.jpg",
                RecordDateUtc = Clock.System.now().toEpochMilliseconds() - Random.nextLong(0, 63072000000),
                Shared = Random.nextLong(0, 2),
                Sku = "SKU-${uniqueId.toString().padStart(6, '0')}",
                State = Random.nextLong(0, 3),
                Tags = listOf("new", "sale", "featured", "popular").shuffled().take(2).joinToString(","),
                TenantId = Random.nextLong(1, 10),
                Vat = listOf(0.0, 8.0, 18.0).random()
            )
        }
    }
    //endregion
}