package com.repzone.network.di
import com.repzone.core.interfaces.ITokenProvider
import com.repzone.network.api.IOrderApi
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.OrderApiImpl
import com.repzone.network.http.impl.TokenApiControllerImpl
import com.repzone.network.http.impl.TokenProviderImpl
import com.repzone.network.http.provideEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module


val NetworkModule = module {
    // ortak config
    single { NetworkConfig(baseUrl = "https://repzoneprodapi-dev-slot-a.azurewebsites.net") }

    // Platform motoru (Android/iOS ayrı modülde sağlanır)
    //single<HttpClientEngine> { provideEngine() }

    single<HttpClient> {
        HttpClientFactory(
            cfg = get(),
            engineProvider = { get<HttpClientEngine>() },
            tokenProvider = null,
            onUnauthenticated = getOrNull()
        ).create()
    }

    // API’ler: parametresiz -> HttpClient'i DI'dan alır
    factory<IOrderApi> { OrderApiImpl(get()) }
    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}