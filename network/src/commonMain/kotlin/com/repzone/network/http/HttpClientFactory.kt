package com.repzone.network.http

import com.repzone.core.interfaces.ITokenProvider
import com.repzone.network.util.PascalCaseNamingStrategy
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.HttpResponseReceived
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@OptIn(ExperimentalSerializationApi::class)
class HttpClientFactory(
    private val cfg: NetworkConfig,
    private val engineProvider: () -> HttpClientEngine,
    private val tokenProvider: ITokenProvider?,
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
                    encodeDefaults = true
                    prettyPrint = true // Debug için
                    namingStrategy = PascalCaseNamingStrategy
                }
            )
        }

        // Varsayılan header + base URL
        install(DefaultRequest) {
            url(cfg.baseUrl) // "https://api.example.com" gibi tam base
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.ContentType, "application/json")
            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoidHJkZW1vdXNlcjZAcmVwLnpvbmUiLCJUZW5hbnRJZCI6IjI1MCIsIk9yZ2FuaXphdGlvbklkIjoiNTY2IiwiUmVwcmVzZW50YXRpdmVJZCI6IjM1NDg3IiwiVXNlcklkIjoiMzk3NDMiLCJpZGVudGl0eVVzZXJJZCI6IjQyNTEwIiwiRW1haWwiOiJ0cmRlbW91c2VyNkByZXAuem9uZSIsIlByb2ZpbGVJZCI6IjAiLCJGdWxsTmFtZSI6IkRlbW8gVXNlciA2IiwiQ3VsdHVyZSI6InRyLVRSIiwiUGhvdG9QYXRoIjoiIiwiSWRlbnRpdHlUZW5hbnRJZCI6IjI5MCIsIk93bmVyVXNlcklkIjoiMzk3NDMiLCJPd25lclVzZXJJZGVudGl0eUlkIjoiNDI1MTAiLCJSb2xlIjoiUmVwcmVzZW50YXRpdmUiLCJOZWVkVG9DaGFuZ2VQYXNzd29yZCI6IjAiLCJJcENvbnRyb2wiOiIwIiwiZXhwIjoxNzYxOTc4NjQ2LCJpc3MiOiJodHRwczovL3BvcnRhbC5yZXB6b25lLmNvbSIsImF1ZCI6Imh0dHBzOi8vcG9ydGFsLnJlcHpvbmUuY29tIn0.-mVqB1H-fQ83yoO2AQfqyeD1NRSsjcaISxAQtjNtwfw"
            header(HttpHeaders.Authorization, "Bearer $token")
            /*tokenProvider?.getToken()?.takeIf { it.isNotBlank() }?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }*/
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    println("Logger Ktor => $message")
                }
            }
        }

        install(createClientPlugin("RequestBodyLogger") {
            onRequest { request, content ->
                println("\n" + "=".repeat(50))
                println("📤 REQUEST")
                println("=".repeat(50))
                println("URL: ${request.url.buildString()}")
                println("Method: ${request.method.value}")
                println("\nHeaders:")
                request.headers.entries().forEach { (key, values) ->
                    if (key != HttpHeaders.Authorization) { // Token'ı loglama
                        println("  $key: ${values.joinToString()}")
                    }
                }

                println("\nBody:")
                when (content) {
                    is OutgoingContent.ByteArrayContent -> {
                        val bodyText = content.bytes().decodeToString()
                        println(bodyText)
                    }
                    is TextContent -> {
                        println(content.text)
                    }
                    else -> {
                        println("[${content::class.simpleName}]")
                    }
                }
                println("=".repeat(50) + "\n")
            }
        })

        HttpResponseValidator {
            validateResponse { resp ->

                if (resp.status == HttpStatusCode.Unauthorized ||
                    resp.status == HttpStatusCode.Forbidden) {
                    // refresh token denemesi (varsa)
                    val refreshed = tokenProvider?.refreshToken()
                    if (refreshed == false) {
                        // artık oturum geçersiz: UI tarafına haber ver
                        onUnauthenticated?.invoke()
                        //throw UnauthenticatedException()
                    }
                }
            }

            handleResponseExceptionWithRequest { cause, request ->
                when (cause) {
                    is io.ktor.client.call.NoTransformationFoundException -> {
                        // 204 durumunda bu exception gelir, yoksay
                        println("ℹ️ NoContent (204) handled for ${request.url}")
                        // throw etmiyoruz, böylece çağıran taraf null/empty alır
                    }
                    else -> throw cause
                }
            }
        }
    }
}