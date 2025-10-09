package com.repzone.network.di

import com.repzone.core.constant.NetWorkModuleConstant
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PlatformNetworkModule = module {
    single<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V1)) { OkHttp.create() }
    single<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V2)) { OkHttp.create() }
}