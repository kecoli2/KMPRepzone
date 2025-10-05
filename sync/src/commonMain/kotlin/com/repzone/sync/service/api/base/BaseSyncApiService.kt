package com.repzone.sync.service.api.base

import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.toModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.ISyncApiService
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseSyncApiService<TDto : Any>(val client: HttpClient) : ISyncApiService<TDto> {
    protected abstract fun extractLastId(data: TDto): Int
    protected abstract fun getDataSize(data: TDto): Int

    override suspend fun fetchAll(model: SyncModuleModel): ApiResult<TDto> {
        throw NotImplementedError("Paging page kullanılacak")
    }

    override suspend fun fetchPage(model: SyncModuleModel, pageSize: Int): Flow<ApiResult<TDto>> = flow {
        val requestModel = model.requestFilter?.toModel<FilterModelRequest>()
        var hasMore = true

        setupRequestFilter(model, requestModel)

        while (hasMore) {
            try {
                val response = performApiCall(model, requestModel)
                when (response) {
                    is ApiResult.Error -> {
                        emit(response)
                        hasMore = false
                    }
                    is ApiResult.Loading -> {
                        // emit loading state
                    }
                    is ApiResult.Success -> {
                        val data = response.data
                        val dataSize = getDataSize(data)

                        if (dataSize == 0) {
                            hasMore = false
                        } else {
                            emit(ApiResult.Success(data))
                            hasMore = dataSize >= pageSize
                            requestModel?.lastId = extractLastId(data)
                            onPageFetched(data, requestModel)
                        }
                    }
                }
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex.toApiException()))
                hasMore = false
            }
        }
    }
    protected abstract suspend fun performApiCall(model: SyncModuleModel, requestModel: FilterModelRequest?): ApiResult<TDto>
    protected open fun setupRequestFilter(model: SyncModuleModel, requestModel: FilterModelRequest?) {
        if (model.lastSyncDate == null) {
            requestModel?.fetchOnlyActive = true
        } else {
            requestModel?.lastModDate =
                model.lastSyncDate?.toDateString("yyyy-MM-dd HH:mm:ss.fff")
        }
    }
    protected open fun onPageFetched(data: TDto, requestModel: FilterModelRequest?) {
    }
}