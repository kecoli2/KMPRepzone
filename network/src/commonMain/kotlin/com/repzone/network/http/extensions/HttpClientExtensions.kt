package com.repzone.network.http.extensions

import com.repzone.network.http.wrapper.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend inline fun <reified T> HttpClient.safePost(url: String, block: HttpRequestBuilder.() -> Unit = {}): ApiResult<T> = try {
    val response: HttpResponse = post(url) {
        block()
    }
    handleResponse<T>(response)
} catch (e: NoTransformationFoundException) {
    handleNoContent<T>()
} catch (e: Exception) {
    ApiResult.Error(e.toApiException())
}

suspend inline fun <reified T> HttpClient.safeGet(url: String, block: HttpRequestBuilder.() -> Unit = {}): ApiResult<T> = try {
    val response: HttpResponse = get(url, block)
    handleResponse<T>(response)
} catch (e: NoTransformationFoundException) {
    handleNoContent<T>()
} catch (e: Exception) {
    ApiResult.Error(e.toApiException())
}
suspend inline fun <reified T> handleResponse(response: HttpResponse): ApiResult<T> {
    return if (response.status == HttpStatusCode.NoContent) {
        handleNoContent<T>()
    } else {
        ApiResult.Success(response.body<T>())
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> handleNoContent(): ApiResult<T> {
    val defaultValue: T = when (T::class) {
        List::class -> emptyList<Any>() as T
        Unit::class -> Unit as T
        else -> null as T
    }
    return ApiResult.Success(defaultValue)
}