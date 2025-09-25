package com.repzone.network.http.extensions

import com.repzone.network.http.wrapper.ApiException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Extension function to map any Exception to ApiException
 */
suspend fun Exception.toApiException(): ApiException = when (this) {
    is ClientRequestException -> this.toClientException()
    is ServerResponseException -> ApiException.ServerError
    is ConnectTimeoutException,
    is SocketTimeoutException,
    is UnresolvedAddressException -> ApiException.NetworkError
    is SerializationException -> ApiException.ValidationError(listOf("Invalid response format"))
    else -> ApiException.UnknownError(this.message ?: "Unknown error")
}

/**
 * Extension function specifically for ClientRequestException
 */
private suspend fun ClientRequestException.toClientException(): ApiException {
    val errorMessage = try {
        response.bodyAsText()
    } catch (e: Exception) {
        message ?: "Client error"
    }

    return when (response.status) {
        HttpStatusCode.Unauthorized -> ApiException.Unauthorized
        HttpStatusCode.Forbidden -> ApiException.Unauthorized
        HttpStatusCode.BadRequest -> {
            val errors = parseValidationErrors(errorMessage)
            ApiException.ValidationError(errors)
        }
        HttpStatusCode.NotFound -> ApiException.ValidationError(listOf("Resource not found"))
        HttpStatusCode.UnprocessableEntity -> {
            val errors = parseValidationErrors(errorMessage)
            ApiException.ValidationError(errors)
        }
        else -> ApiException.UnknownError(errorMessage)
    }
}

/**
 * Parse validation errors from response body
 */
private fun parseValidationErrors(errorBody: String): List<String> {
    return try {
        // Backend formatina gore bir validation yazılacak
        /*when {
            errorBody.contains("errors") -> {
                Json.decodeFromString<ValidationErrorResponse>(errorBody).errors
            }
            errorBody.contains("message") -> {
                Json.decodeFromString<SingleErrorResponse>(errorBody).message?.let { listOf(it) } ?: listOf("Validation failed")
            }
            else -> listOf(errorBody.ifEmpty { "Validation failed" })
        }*/
        listOf("Validation failed")
    } catch (e: Exception) {
        listOf("Validation failed")
    }
}