package com.repzone.domain.common

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: DomainException) : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
    }

    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> data
        is Error -> defaultValue
    }

    inline fun getOrElse(onError: (DomainException) -> @UnsafeVariance T): @UnsafeVariance T = when (this) {
        is Success -> data
        is Error -> onError(exception)
    }
}

inline fun <T> Result<T>.onSuccess(block: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        block(data)
    }
    return this
}

inline fun <T> Result<T>.onError(block: (DomainException) -> Unit): Result<T> {
    if (this is Result.Error) {
        block(exception)
    }
    return this
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
}

inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
    is Result.Success -> transform(data)
    is Result.Error -> this
}

inline fun <T, R> Result<T>.fold(onSuccess: (T) -> R, onError: (DomainException) -> R): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(exception)
}

fun <T> T?.toResult(entityName: String = "Value", entityId: Any = "unknown"): Result<T> =
    this?.let { Result.Success(it) } ?: Result.Error(
        DomainException.NotFoundException(entityName, entityId)
    )

inline fun <T> resultOf(block: () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: DomainException) {
    Result.Error(e)
} catch (e: Exception) {
    Result.Error(DomainException.UnknownException(cause = e))
}

suspend inline fun <T> suspendResultOf(crossinline block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: DomainException) {
    Result.Error(e)
} catch (e: Exception) {
    Result.Error(DomainException.UnknownException(cause = e))
}