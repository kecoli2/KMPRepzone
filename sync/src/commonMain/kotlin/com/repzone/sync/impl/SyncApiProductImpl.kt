package com.repzone.sync.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiProductImpl(client: HttpClient) : BaseSyncApiService<List<MobileProductDto>>(client) {

    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<MobileProductDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<MobileProductDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<MobileProductDto>> {
        return client.safePost<List<MobileProductDto>>(model.requestUrl!!) {
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}