package com.repzone.sync.service.api.impl

import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class SyncApiModulesImpl(httpClient: HttpClient) : BaseSyncApiService<List<PackageCustomFieldDto>>(httpClient) {
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

    override fun setupRequestFilter(model: SyncModuleModel, requestModel: FilterModelRequest?) {
        requestModel?.lastId = 0
        requestModel?.fetchOnlyActive = true
        requestModel?.lastModDate = ""

    }
    //endregion

}