package com.repzone.sync.impl

import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.toModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.dto.MobileRouteDto
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.ISyncApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncApiRouteDataImpl(private val client: HttpClient) : ISyncApiService<List<MobileRouteDto>>{
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override suspend fun fetchAll(model: SyncModuleModel): ApiResult<List<MobileRouteDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPage(model: SyncModuleModel, pageSize: Int, ): Flow<ApiResult<List<MobileRouteDto>>>  = flow {
        val requestModel = model.requestFilter?.toModel<FilterModelRequest>()
        var currentPAge = 0
        var hasMore = true
        if(model.lastSyncDate == null){
            requestModel?.fetchOnlyActive = true
        }else{
            requestModel?.lastModDate = model.lastSyncDate?.toDateString("yyyy-MM-dd HH:mm:ss.fff")
        }

        while (hasMore){
            try {

                val response = client.safePost<List<MobileRouteDto>>(model.requestUrl!!){
                    setBody(requestModel)
                }
                when(response){
                    is ApiResult.Error -> {
                        throw Exception("API Error: ${response.exception.message}")
                    }
                    is ApiResult.Loading -> {
                    }
                    is ApiResult.Success -> {
                        val data = response.data
                        if(data.isEmpty()){
                            hasMore = false
                        }else{
                            emit(ApiResult.Success(data))
                            currentPAge++
                            hasMore = data.size >= pageSize
                            requestModel?.lastId = data.last().id
                        }
                    }
                }
            }catch (ex: Exception){
                emit(ApiResult.Error(ex.toApiException()))
                hasMore = false
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}