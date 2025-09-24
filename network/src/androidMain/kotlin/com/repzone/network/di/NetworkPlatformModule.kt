package com.repzone.network.di

import com.repzone.network.http.AndroidHttpEngineProvider
import com.repzone.network.http.HttpEngineProvider
import org.koin.dsl.module

val NetworkPlatformModule = module {
    single<HttpEngineProvider> { AndroidHttpEngineProvider() }
}