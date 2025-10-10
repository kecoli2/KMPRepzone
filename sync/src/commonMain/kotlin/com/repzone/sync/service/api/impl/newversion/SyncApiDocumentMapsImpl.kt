package com.repzone.sync.service.api.impl.newversion

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiDocumentMapsImpl(client: HttpClient): BaseSyncApiService<List<DocumentMapModelDto>>(client) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<DocumentMapModelDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<DocumentMapModelDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<DocumentMapModelDto>> {
        return client.safePost<List<DocumentMapModelDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}