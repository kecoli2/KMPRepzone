package com.repzone.sync.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.CustomerDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiCustomerImpl(client: HttpClient) : BaseSyncApiService<List<CustomerDto>>(client) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method


    override fun extractLastId(data: List<CustomerDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<CustomerDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<CustomerDto>> {
        return client.safePost<List<CustomerDto>>(model.requestUrl!!) {
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}