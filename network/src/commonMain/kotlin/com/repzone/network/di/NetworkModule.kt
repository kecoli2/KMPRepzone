package com.repzone.network.di
import com.repzone.network.api.IOrderApi
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.TokenProvider
import com.repzone.network.http.impl.OrderApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module

val NetworkModule = module{
    // Base config (istersen gradle.properties'ten oku)
    single { NetworkConfig(baseUrl = "https://api.example.com") }

    // HttpClient factory: engine ve token provider APP katmanından parametreyle gelecek
    factory { (engine: HttpClientEngine, tokenProvider: TokenProvider?) ->
        HttpClientFactory(get(), { engine }).create(tokenProvider)
    }

    // API implementasyonları: HttpClient parametreyle verilir
    factory<IOrderApi> { (client: HttpClient) -> OrderApiImpl(client) }
}