package com.repzone.sync.service.api.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiCustomerGroupImpl(client: HttpClient) : BaseSyncApiService<List<CustomerGroupDto>>(client) {
    //region Public Method
    override fun extractLastId(data: List<CustomerGroupDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<CustomerGroupDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<CustomerGroupDto>> {
        return client.safePost<List<CustomerGroupDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }
    //endregion

}