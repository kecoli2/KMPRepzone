package com.repzone.sync.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.model.SyncPage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.coroutines.delay
import kotlin.random.Random

class SyncApiCustomerImpl(private val client: HttpClient): ISyncApiService<SyncCustomerModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun fetchAll(): ApiResult<List<SyncCustomerModel>> {
        return try{
            //delay(5000)
            /*val response = client.post(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
            }
            ApiResult.Success(response.body<List<SyncCustomerModel>>())*/
            ApiResult.Success(generate100KCustomers())
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }

    override suspend fun fetchUpdatedSince(sinceIso: String): ApiResult<List<SyncCustomerModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPage(page: Int, size: Int): ApiResult<SyncPage<SyncCustomerModel>> {
        TODO("Not yet implemented")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method

    fun generate100KCustomers(startId: Long = 1): List<SyncCustomerModel> {
        return List(20_000) { index ->
            val uniqueId = startId + index
            SyncCustomerModel(
                Id = uniqueId,
                Balance = Random.nextDouble(-10000.0, 50000.0),
                Blocked = Random.nextLong(0, 2),
                CheckRiskDispatch = Random.nextLong(0, 2),
                CheckRiskOnBalance = Random.nextLong(0, 2),
                CheckRiskOrder = Random.nextLong(0, 2),
                CheckRiskOrderProposal = Random.nextLong(0, 2),
                Code = "CUST-${uniqueId.toString().padStart(6, '0')}",
                DamagedReturnPriceType = Random.nextLong(0, 3),
                DispatchMonitoringAction = Random.nextLong(0, 5),
                GroupId = Random.nextLong(1, 100),
                GroupName = "Customer Group ${Random.nextInt(1, 100)}",
                GroupPhotoPath = "/photos/customer-groups/${Random.nextInt(1, 100)}.jpg",
                IconIndex = Random.nextLong(0, 50),
                InvoiceMonitoringAction = Random.nextLong(0, 5),
                IsECustomer = Random.nextLong(0, 2),
                IsVatExempt = Random.nextLong(0, 2),
                IsVisible = Random.nextLong(0, 2),
                Name = "Customer $uniqueId",
                OrderMonitoringAction = Random.nextLong(0, 5),
                OrderProposalMonitoringAction = Random.nextLong(0, 5),
                OrganizationCode = "ORG-${Random.nextInt(1000, 9999)}",
                OrganizationId = Random.nextLong(1, 50),
                OrganizationName = "Organization ${Random.nextInt(1, 50)}",
                ParentId = if (Random.nextBoolean()) Random.nextLong(1, 1000) else null,
                PaymentPlanId = Random.nextLong(1, 20),
                PhotoPath = "/photos/customers/$uniqueId.jpg",
                ReturnPriceType = Random.nextLong(0, 3),
                Risk = Random.nextDouble(0.0, 100000.0),
                RiskDueDay = Random.nextLong(0, 90),
                State = Random.nextLong(0, 3),
                Tags = listOf("vip", "wholesale", "retail", "new").shuffled().take(2).joinToString(","),
                TaxNumber = (uniqueId + 10000000000).toString().padStart(11, '0'),
                TaxOffice = "Tax Office ${Random.nextInt(1, 100)}"
            )
        }
    }
    //endregion
}