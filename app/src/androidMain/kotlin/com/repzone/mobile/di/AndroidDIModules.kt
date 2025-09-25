package com.repzone.mobile.di

import com.repzone.core.interfaces.ILocationService
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.ITokenProvider
import com.repzone.mobile.managers.location.LocationServiceAndroid
import com.repzone.mobile.managers.pref.AndroidPreferencesManager
import com.repzone.mobile.managers.pref.AndroidPreferencesManagerPreview
import com.repzone.network.http.impl.TokenProviderImpl
import org.koin.dsl.module

val AndroidDIModule = module {
    single<ILocationService> { LocationServiceAndroid(app = get()) }
    single<IPreferencesManager>{ AndroidPreferencesManager(context = get()) }
    single<ITokenProvider> { TokenProviderImpl(get(), get()) }
}

val AndroidDIModulePreview = module {
    single<ILocationService> { LocationServiceAndroid(app = get()) }
    single<IPreferencesManager>{ AndroidPreferencesManagerPreview() }
    single<ITokenProvider> { TokenProviderImpl(get(), get()) }
}