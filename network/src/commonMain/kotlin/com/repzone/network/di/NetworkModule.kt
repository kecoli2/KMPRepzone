package com.repzone.network.di
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.TokenApiControllerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module


val NetworkModule = module {
    // ortak config
    //single { NetworkConfig(baseUrl = "https://repzoneprodapi-dev-slot-a.azurewebsites.net") }
    single { NetworkConfig(baseUrl = "https://repzone-mobile-api.azurewebsites.net/api") }

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
    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}