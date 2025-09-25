package com.repzone.network.http.wrapper

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: ApiException) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}


sealed class ApiException(message: String) : Exception(message) {
    object NetworkError : ApiException("Network connection failed")
    object Unauthorized : ApiException("Authentication required")
    object ServerError : ApiException("Server error occurred")
    data class ValidationError(val errors: List<String>) : ApiException("Validation failed")
    data class UnknownError(val originalMessage: String?) : ApiException("Unknown error: $originalMessage")
}