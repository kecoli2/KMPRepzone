package com.repzone.network.http

import com.repzone.core.interfaces.ITokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val cfg: NetworkConfig,
    private val engineProvider: () -> HttpClientEngine,
    private val tokenProvider: ITokenProvider,
    private val onUnauthenticated: (() -> Unit)? = null  // 401'de çağrılacak
) {
    fun create(): HttpClient = HttpClient(engineProvider()) {
        // Zaman aşımı
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis  = 30_000
        }

        // JSON
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                }
            )
        }

        // Varsayılan header + base URL
        install(DefaultRequest) {
            url(cfg.baseUrl) // "https://api.example.com" gibi tam base
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.ContentType, "application/json")
            tokenProvider.getToken()?.takeIf { it.isNotBlank() }?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
        }

        // Logging (geliştirmede faydalı)
        install(Logging) {
            level = LogLevel.INFO
        }

        // 401/403 yakalama
        HttpResponseValidator {
            validateResponse { resp ->
                if (resp.status == HttpStatusCode.Unauthorized ||
                    resp.status == HttpStatusCode.Forbidden) {
                    // refresh token denemesi (varsa)
                    val refreshed = tokenProvider.refreshToken()
                    if (!refreshed) {
                        // artık oturum geçersiz: UI tarafına haber ver
                        onUnauthenticated?.invoke()
                        //throw UnauthenticatedException()
                    }
                }
            }

            handleResponseExceptionWithRequest { cause, _ ->
                // Burada cause -> IOException, Serialization vb. eşleyebilirsiniz
                throw cause
            }
        }

        // 401 sonrası otomatik tekrar deneme (opsiyonel)
        /*install(HttpSend) {
            intercept { request ->
                val response = execute(request)
                if (response.status == HttpStatusCode.Unauthorized) {
                    val refreshed = tokenProvider.refreshToken()
                    if (refreshed) {
                        // Header’ı güncelle, tekrar dene
                        request.headers.remove(HttpHeaders.Authorization)
                        tokenProvider?.getToken()?.takeIf { it.isNotBlank() }?.let {
                            request.headers.append(HttpHeaders.Authorization, "Bearer $it")
                        }
                        return@intercept execute(request)
                    } else {
                        onUnauthenticated?.invoke()
                    }
                }
                response
            }*/
        }
}