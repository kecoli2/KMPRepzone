package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.SyncUnitDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiProductUnitImpl(client: HttpClient) : BaseSyncApiService<List<SyncUnitDto>>(client) {
    //region Public Method
    override fun getDataSize(data: List<SyncUnitDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<SyncUnitDto>> {
        return client.safePost<List<SyncUnitDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }


    override fun onPageFetched(data: List<SyncUnitDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff", TimeZone.UTC) ?: ""
            requestModel?.lastId = it?.id ?: 0
        }
    }
    //endregion
}