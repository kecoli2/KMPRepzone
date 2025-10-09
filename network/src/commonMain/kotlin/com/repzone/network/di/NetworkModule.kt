package com.repzone.network.di
import com.repzone.core.config.BuildConfig
import com.repzone.core.constant.NetWorkModuleConstant
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.HttpClientFactory
import com.repzone.network.http.NetworkConfig
import com.repzone.network.http.impl.TokenApiControllerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.qualifier.named
import org.koin.dsl.module


val NetworkModule = module {

    //region V1 CLIENT
    single (named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V1)) { NetworkConfig(baseUrl = BuildConfig.endpointV1) }

    single<HttpClient>(named(NetWorkModuleConstant.HTTP_CLIENT_V1)) {
        HttpClientFactory(
            cfg = get(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V1)),
            engineProvider = { get<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V1)) },
            tokenProvider = null,
            onUnauthenticated = getOrNull()
        ).create()
    }
    //endregion V1 CLIENT

    //region V2 CLIENT
    single (named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V2)) { NetworkConfig(baseUrl = BuildConfig.endpointV2) }

    single<HttpClient>(named(NetWorkModuleConstant.HTTP_CLIENT_V2)) {
        HttpClientFactory(
            cfg = get(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V2)),
            engineProvider = { get<HttpClientEngine>(named(NetWorkModuleConstant.HTTP_CLIENT_CONFIG_V2)) },
            tokenProvider = null,
            onUnauthenticated = getOrNull()
        ).create()
    }
    //endregion V2 CLIENT

    factory<ITokenApiController>(named("v1")) { TokenApiControllerImpl(get(named(NetWorkModuleConstant.HTTP_CLIENT_V1))) }
    factory<ITokenApiController>(named("v2")) { TokenApiControllerImpl(get(named(NetWorkModuleConstant.HTTP_CLIENT_V2))) }
}