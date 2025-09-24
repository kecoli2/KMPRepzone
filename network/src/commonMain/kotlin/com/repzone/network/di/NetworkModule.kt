package com.repzone.network.di
import com.repzone.network.api.IOrderApi
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.TokenProvider
import com.repzone.network.http.impl.OrderApiImpl
import com.repzone.network.http.impl.TokenApiControllerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import org.koin.dsl.module

val NetworkModule = module {
    // Base config
    single { NetworkConfig(baseUrl = "https://api.example.com") }

    // HttpClient (engineProvider + tokenProvider parametreli)
    factory { (engineProvider: () -> HttpClientEngine, tokenProvider: TokenProvider?) ->
        HttpClientFactory(get(), engineProvider).create(tokenProvider)
    }

    // API’ler: HttpClient parametreli
    factory<IOrderApi> { (client: HttpClient) -> OrderApiImpl(client) }
    factory<ITokenApiController> { (client: HttpClient) -> TokenApiControllerImpl(client) }
}