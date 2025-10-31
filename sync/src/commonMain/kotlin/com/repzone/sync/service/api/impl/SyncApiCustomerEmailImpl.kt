package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiCustomerEmailImpl(client: HttpClient) : BaseSyncApiService<List<CustomerEmailDto>>(client) {
    //region Public Method
    override fun getDataSize(data: List<CustomerEmailDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<CustomerEmailDto>> {
        return client.safePost<List<CustomerEmailDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }

    override fun onPageFetched(data: List<CustomerEmailDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""
            requestModel?.lastId = it?.id ?: 0
        }
    }
    //endregion

}