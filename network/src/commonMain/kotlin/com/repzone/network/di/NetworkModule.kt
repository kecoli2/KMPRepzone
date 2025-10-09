package com.repzone.network.di
import com.repzone.core.config.BuildConfig
import com.repzone.core.config.UIModule
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

    includes(
        when(BuildConfig.activeUIModule){
            UIModule.NEW -> {
                NetworkModuleV2
            }
            UIModule.LEGACY -> {
                NetworkModuleV1
            }
        }
    )

}