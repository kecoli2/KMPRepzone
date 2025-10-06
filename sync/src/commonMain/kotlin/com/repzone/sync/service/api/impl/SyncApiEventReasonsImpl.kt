package com.repzone.sync.service.api.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiEventReasonsImpl(client: HttpClient) : BaseSyncApiService<List<EventReasonDto>>(client) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<EventReasonDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<EventReasonDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<EventReasonDto>> {
        return client.safePost<List<EventReasonDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}