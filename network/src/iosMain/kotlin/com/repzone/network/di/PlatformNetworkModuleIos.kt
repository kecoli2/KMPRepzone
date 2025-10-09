package com.repzone.network.di

import com.repzone.core.constant.NetWorkModuleConstant
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PlatformNetworkModuleIos = module {
    single<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V1)) { Darwin.create() }
    single<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V2)) { Darwin.create() }
}