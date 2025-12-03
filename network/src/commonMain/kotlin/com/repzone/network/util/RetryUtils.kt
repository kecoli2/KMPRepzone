package com.repzone.network.util

import com.repzone.core.platform.Logger
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.delay
import kotlinx.io.IOException

suspend fun <T> retryWithBackoff(maxRetries: Int = 3, initialDelayMs: Long = 1000, maxDelayMs: Long = 10000, factor: Double = 2.0, retryOn: (Exception) -> Boolean = ::isRetryableException,block: suspend () -> T): T {
    var currentDelay = initialDelayMs
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            lastException = e

            if (!retryOn(e)) {
                throw e // Retry yapılmaması gereken hata
            }

            if (attempt < maxRetries - 1) {
                Logger.d("Attempt ${attempt + 1}/$maxRetries failed: ${e.message}")
                Logger.d("Retrying in ${currentDelay}ms...")
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMs)
            }
        }
    }

    throw lastException ?: IllegalStateException("Retry failed")
}

fun isRetryableException(e: Exception): Boolean {
    return when (e) {
        is SocketTimeoutException -> true  // Timeout
        is IOException -> true             // Genel I/O hataları
        else -> {
            val message = e.message?.lowercase() ?: ""
            message.contains("timeout") ||
                    message.contains("connection") ||
                    message.contains("refused")
        }
    }
}