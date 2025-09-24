package com.repzone.network.di

import com.repzone.network.http.HttpEngineProvider
import com.repzone.network.http.IosHttpEngineProvider
import org.koin.dsl.module

val NetworkPlatformModule = module {
    single<HttpEngineProvider> { IosHttpEngineProvider() }
}