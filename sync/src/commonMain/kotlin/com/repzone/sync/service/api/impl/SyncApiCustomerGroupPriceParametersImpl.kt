package com.repzone.sync.service.api.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.service.api.base.BaseSyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncApiCustomerGroupPriceParametersImpl(client: HttpClient): BaseSyncApiService<List<CrmPriceListParameterDto>>(client) {
    //region Public Method
    override fun getDataSize(data: List<CrmPriceListParameterDto>): Int {
        return data.size
    }

    override suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<List<CrmPriceListParameterDto>> {
        return client.safePost<List<CrmPriceListParameterDto>>(model.requestUrl!!){
            setBody(requestModel)
        }
    }

    override fun onPageFetched(data: List<CrmPriceListParameterDto>, requestModel: FilterModelRequest?) {
        data.lastOrNull().let {
            requestModel?.lastModDate = it?.modificationDateUtc?.toEpochMilliseconds()?.toDateString("yyyy-MM-dd HH:mm:ss.fff") ?: ""
            requestModel?.lastId = it?.id ?: 0
        }
    }
    //endregion

}