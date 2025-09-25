package com.repzone.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val PlatformNetworkModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}