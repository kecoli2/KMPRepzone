package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.RouteDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiRouteDataImpl(client: HttpClient) : BaseSyncApiService<List<RouteDto>>(client){

    //region Public Method
    override fun getDataSize(data: List<RouteDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<RouteDto>> {
        return client.safePost<List<RouteDto>>(model.requestUrl!!) {
            setBody(requestModel)
        }
    }

    override fun onPageFetched(data: List<RouteDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastId = it?.id ?: 0
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff",
                TimeZone.UTC)
        }
    }
    //endregion

}