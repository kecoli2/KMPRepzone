package com.repzone.network.di


import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

val PlatformNetworkModuleIos = module {
    single<HttpClientEngine> { Darwin.create() }
}