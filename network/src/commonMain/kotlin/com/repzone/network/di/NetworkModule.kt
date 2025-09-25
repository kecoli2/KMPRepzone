package com.repzone.network.di
import com.repzone.network.api.IOrderApi
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.OrderApiImpl
import com.repzone.network.http.impl.TokenApiControllerImpl
import com.repzone.network.http.provideEngine
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module


val NetworkModule = module {
    // ortak config
    single { NetworkConfig(baseUrl = "https://api.example.com") }

    // Platform motoru (Android/iOS ayrı modülde sağlanır)
    single<HttpClientEngine> { provideEngine() }

    // API’ler: parametresiz -> HttpClient'i DI'dan alır
    factory<IOrderApi> { OrderApiImpl(get()) }
    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}