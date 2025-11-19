package com.repzone.mobile.di

import android.system.Os.bind
import com.repzone.core.interfaces.IDeviceInfo
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.domain.platform.IPlatformLocationProvider
import com.repzone.domain.platform.IPlatformServiceController
import com.repzone.domain.platform.providerImpl.AndroidLocationProvider
import com.repzone.domain.service.IPlatformGeocoder
import com.repzone.mobile.impl.AndroidGeocoderImpl
import com.repzone.mobile.impl.DeviceInfoImpl
import com.repzone.mobile.managers.pref.AndroidPreferencesManager
import com.repzone.mobile.managers.pref.AndroidPreferencesManagerPreview
import com.repzone.mobile.platform.AndroidServiceController
import com.repzone.platform.FirebaseFactory
import com.repzone.platform.FirebaseManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AndroidDIModule = module {
    single<IPreferencesManager>{ AndroidPreferencesManager(context = get()) }
    singleOf(::AndroidLocationProvider) { bind<IPlatformLocationProvider>() }
    singleOf(::AndroidServiceController) { bind<IPlatformServiceController>() }
    singleOf(::DeviceInfoImpl) {bind<IDeviceInfo>()}
    singleOf(::AndroidGeocoderImpl) { bind<IPlatformGeocoder>() }
}

val AndroidDIModulePreview = module {
    single<IPreferencesManager>{ AndroidPreferencesManagerPreview() }
}

val FirebaseAndroidModule = module {
    single { FirebaseFactory() }
    single<IFirebaseRealtimeDatabase> { get<FirebaseFactory>().createRealtimeDatabase() }
    single<IFirebaseCrashlytics> { get<FirebaseFactory>().createCrashlytics() }
    single<IFirebaseMessaging> { get<FirebaseFactory>().createMessaging() }
    singleOf(::FirebaseManager)
}

val FirebaseMockAndroidModule = module {

}