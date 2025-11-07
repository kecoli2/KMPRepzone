package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.SyncMandatoryFormDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiFormMandatoryDataFetchImpl(client: HttpClient) : BaseSyncApiService<List<SyncMandatoryFormDto>>(client) {
    //region Public Method
    override fun getDataSize(data: List<SyncMandatoryFormDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<SyncMandatoryFormDto>> {
        return client.safePost<List<SyncMandatoryFormDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }

    override fun onPageFetched(data: List<SyncMandatoryFormDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff",
                TimeZone.UTC) ?: ""
            requestModel?.lastId = it?.id ?: 0
        }
    }
    //endregion

}