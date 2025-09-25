package com.repzone.network.http.extensions

import com.repzone.network.http.wrapper.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend inline fun <reified T> HttpClient.safePost(url: String, block: HttpRequestBuilder.() -> Unit = {}): ApiResult<T> = try {
    val response = post(url) {
        contentType(ContentType.Application.Json)
        block()
    }
    ApiResult.Success(response.body<T>())
} catch (e: Exception) {
    ApiResult.Error(e.toApiException()) // Artık suspend
}

suspend inline fun <reified T> HttpClient.safeGet(url: String, block: HttpRequestBuilder.() -> Unit = {}): ApiResult<T> = try {
    val response = get(url, block)
    ApiResult.Success(response.body<T>())
} catch (e: Exception) {
    ApiResult.Error(e.toApiException()) // Artık suspend
}