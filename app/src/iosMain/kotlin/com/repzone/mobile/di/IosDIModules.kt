package com.repzone.mobile.di

import com.repzone.core.interfaces.ILocationService
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.mobile.manager.pref.IOSPreferencesManager
import org.koin.dsl.module

val IosDIModule = module {
    single<IPreferencesManager>{ IOSPreferencesManager() }
}