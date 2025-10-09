package com.repzone.network.di

import com.repzone.core.config.BuildConfig
import com.repzone.core.constant.NetWorkModuleConstant
import com.repzone.core.constant.VersionModuleConstant
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.TokenApiControllerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.qualifier.named
import org.koin.dsl.module


//TUM CAGIRIMLAR NAMED OLMASI LAZIM VS BAZINDA DEGISIMLER SOZKONUSU
internal val NetworkModuleV1 = module {
    single (named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V1)) { NetworkConfig(baseUrl = BuildConfig.endpointV1) }

    single<HttpClient>(named(NetWorkModuleConstant.HTTP_CLIENT_V1)) {
        HttpClientFactory(
            cfg = get(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V1)),
            engineProvider = { get<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V1)) },
            tokenProvider = null,
            onUnauthenticated = getOrNull()
        ).create()
    }

    factory<ITokenApiController>(named(VersionModuleConstant.V1_VERSION)) { TokenApiControllerImpl(get(named(NetWorkModuleConstant.HTTP_CLIENT_V1))) }
}