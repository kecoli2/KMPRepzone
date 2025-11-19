package com.repzone.mobile.di


import com.repzone.core.interfaces.IDeviceInfo
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.domain.service.IPlatformGeocoder
import com.repzone.mobile.impl.IosDeviceInfo
import com.repzone.mobile.impl.IosGeocoderImpl
import com.repzone.mobile.manager.pref.IOSPreferencesManager
import com.repzone.platform.FirebaseFactory
import com.repzone.platform.FirebaseManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val IosDIModule = module {
    single<IPreferencesManager>{ IOSPreferencesManager() }
    single<IDeviceInfo> { IosDeviceInfo() }
    singleOf(::IosGeocoderImpl) { bind<IPlatformGeocoder>() }
}

val FirebaseIosModule = module {
    single { FirebaseFactory() }
    single<IFirebaseRealtimeDatabase> { get<FirebaseFactory>().createRealtimeDatabase() }
    single<IFirebaseCrashlytics> { get<FirebaseFactory>().createCrashlytics() }
    single<IFirebaseMessaging> { get<FirebaseFactory>().createMessaging() }
    singleOf(::FirebaseManager)
}