package com.repzone.sync.impl

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.sync.interfaces.ISyncApiService
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class SyncApiCustomerImpl(private val client: HttpClient): ISyncApiService<List<SyncCustomerModel>> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun fetchAll(model: SyncModuleModel): ApiResult<List<SyncCustomerModel>> {
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

    override suspend fun fetchPage(model: SyncModuleModel, pageSize: Int): Flow<ApiResult<List<SyncCustomerModel>>> {
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
                id = uniqueId,
                balance = Random.nextDouble(-10000.0, 50000.0),
                blocked = Random.nextLong(0, 2),
                checkRiskDispatch = Random.nextLong(0, 2),
                checkRiskOnBalance = Random.nextLong(0, 2),
                checkRiskOrder = Random.nextLong(0, 2),
                checkRiskOrderProposal = Random.nextLong(0, 2),
                code = "CUST-${uniqueId.toString().padStart(6, '0')}",
                damagedReturnPriceType = Random.nextLong(0, 3),
                dispatchMonitoringAction = Random.nextLong(0, 5),
                groupId = Random.nextLong(1, 100),
                groupName = "Customer Group ${Random.nextInt(1, 100)}",
                groupPhotoPath = "/photos/customer-groups/${Random.nextInt(1, 100)}.jpg",
                iconIndex = Random.nextLong(0, 50),
                invoiceMonitoringAction = Random.nextLong(0, 5),
                isECustomer = false,
                isVatExempt = false,
                isVisible = false,
                name = "Customer $uniqueId",
                orderMonitoringAction = Random.nextLong(0, 5),
                orderProposalMonitoringAction = Random.nextLong(0, 5),
                organizationCode = "ORG-${Random.nextInt(1000, 9999)}",
                organizationId = Random.nextLong(1, 50),
                organizationName = "Organization ${Random.nextInt(1, 50)}",
                parentId = if (Random.nextBoolean()) Random.nextLong(1, 1000) else null,
                paymentPlanId = Random.nextLong(1, 20),
                photoPath = "/photos/customers/$uniqueId.jpg",
                returnPriceType = Random.nextLong(0, 3),
                risk = Random.nextDouble(0.0, 100000.0),
                riskDueDay = Random.nextLong(0, 90),
                state = Random.nextLong(0, 3),
                tags = listOf("vip", "wholesale", "retail", "new").shuffled().take(2).joinToString(","),
                taxNumber = (uniqueId + 10000000000).toString().padStart(11, '0'),
                taxOffice = "Tax Office ${Random.nextInt(1, 100)}"
            )
        }
    }
    //endregion
}