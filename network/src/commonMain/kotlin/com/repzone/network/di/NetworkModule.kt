package com.repzone.network.di
import com.repzone.core.config.BuildConfig
import com.repzone.core.enums.UIModule
import com.repzone.core.interfaces.ITokenProvider
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.TokenApiControllerImpl
import com.repzone.network.http.impl.TokenProviderImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module


val NetworkModule = module {
    single { NetworkConfig(baseUrl = BuildConfig.apiEndpoint) }
    single<ITokenProvider> { TokenProviderImpl(get()) }
    single<HttpClient> {
        HttpClientFactory(
            cfg = get(),
            engineProvider = { get<HttpClientEngine>() },
            tokenProvider = get<ITokenProvider>(),
            onUnauthenticated = getOrNull()
        ).create()
    }

    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}