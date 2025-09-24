package com.repzone.network.di
import com.repzone.network.api.IOrderApi
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.HttpEngineProvider
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.TokenProvider
import com.repzone.network.http.impl.OrderApiImpl
import com.repzone.network.http.impl.TokenApiControllerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import org.koin.dsl.module


val NetworkModule = module {
    // ortak config (istersen properties’ten oku)
    single { NetworkConfig(baseUrl = "https://api.example.com") }

    // Ortak HttpClient: platform engine provider'ı enjekte ediyoruz
    single<HttpClient> {
        val cfg: NetworkConfig = get()
        val engine = get<HttpEngineProvider>().get()
        val tokenProvider: TokenProvider? = getOrNull()
        HttpClientFactory(cfg, engineProvider = { engine }).create(tokenProvider)
    }

    // API’ler: parametresiz -> HttpClient'i DI'dan alır
    factory<IOrderApi> { OrderApiImpl(get()) }
    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}