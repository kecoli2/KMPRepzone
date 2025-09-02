package com.repzone.mobile.di

import com.repzone.core.interfaces.ILocationService
import com.repzone.mobile.managers.location.LocationServiceAndroid
import org.koin.dsl.module

val AndroidLocationModule = module {
    single<ILocationService> { LocationServiceAndroid(app = get()) }
}