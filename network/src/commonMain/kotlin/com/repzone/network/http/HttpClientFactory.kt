package com.repzone.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(private val config: NetworkConfig, private val engineProvider: () -> HttpClientEngine) {
    fun create(tokenProvider: TokenProvider? = null): HttpClient =
        HttpClient(engineProvider()) {
            expectSuccess = true

            install(Logging) { level = LogLevel.INFO }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                })
            }

            install(DefaultRequest) {
                url(config.baseUrl)
                tokenProvider?.token()?.takeIf { !it.isNullOrBlank() }?.let { t ->
                    headers.append("Authorization", "Bearer $t")
                }
            }
        }
}