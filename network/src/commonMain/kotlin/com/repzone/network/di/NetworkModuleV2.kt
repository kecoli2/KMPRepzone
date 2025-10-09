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
internal val NetworkModuleV2 = module {
    single (named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V2)) { NetworkConfig(baseUrl = BuildConfig.endpointV2) }

    single<HttpClient>(named(NetWorkModuleConstant.HTTP_CLIENT_V2)) {
        HttpClientFactory(
            cfg = get(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V2)),
            engineProvider = { get<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_ENGINE_V2)) },
            tokenProvider = null,
            onUnauthenticated = getOrNull()
        ).create()
    }

    factory<ITokenApiController>(named(VersionModuleConstant.V2_VERSION)) { TokenApiControllerImpl(get(named(NetWorkModuleConstant.HTTP_CLIENT_V2))) }
}