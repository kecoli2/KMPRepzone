package com.repzone.sync.service.api.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.MobileRouteDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiRouteDataImpl(client: HttpClient) : BaseSyncApiService<List<MobileRouteDto>>(client){

    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<MobileRouteDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<MobileRouteDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<MobileRouteDto>> {
        return client.safePost<List<MobileRouteDto>>(model.requestUrl!!) {
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}