package com.repzone.sync.service.api.impl.newversion

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiModulesImpl(httpClient: HttpClient) : BaseSyncApiService<List<PackageCustomFieldDto>>(httpClient) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(data: List<PackageCustomFieldDto>): Int {
        return data.lastOrNull()?.id ?: 0
    }

    override fun getDataSize(data: List<PackageCustomFieldDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<PackageCustomFieldDto>> {
        return client.safePost<List<PackageCustomFieldDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}