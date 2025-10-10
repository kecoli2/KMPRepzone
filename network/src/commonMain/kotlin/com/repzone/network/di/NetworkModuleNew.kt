package com.repzone.network.di

import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.impl.TokenApiControllerImpl
import org.koin.dsl.module


//TUM CAGIRIMLAR NAMED OLMASI LAZIM VS BAZINDA DEGISIMLER SOZKONUSU
internal val NetworkModuleNew = module {

    factory<ITokenApiController> { TokenApiControllerImpl(get()) }
}