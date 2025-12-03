package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.SyncProductDistributionLineDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiProductDistributionLineImpl(client: HttpClient) : BaseSyncApiService<List<SyncProductDistributionLineDto>>(client) {
    //region Public Method
    override fun getDataSize(data: List<SyncProductDistributionLineDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<SyncProductDistributionLineDto>> {
        return client.safePost<List<SyncProductDistributionLineDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }

    override fun onPageFetched(data: List<SyncProductDistributionLineDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff",
                TimeZone.UTC) ?: ""
            requestModel?.lastId = it?.id ?: 0
        }
    }
    //endregion Public Method
}