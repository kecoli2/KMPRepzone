package com.repzone.sync.service.api.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiProductGroupImpl(client: HttpClient) : BaseSyncApiService<List<ServiceProductGroupDto>>(client) {

    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<ServiceProductGroupDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<ServiceProductGroupDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<ServiceProductGroupDto>> {
        return client.safePost<List<ServiceProductGroupDto>>(model.requestUrl!!) {
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}