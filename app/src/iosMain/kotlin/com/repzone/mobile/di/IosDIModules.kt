package com.repzone.mobile.di

import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import com.repzone.core.interfaces.IFirebaseCrashService
import com.repzone.core.interfaces.IFirebasePushService
import com.repzone.core.interfaces.ILocationService
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.mobile.firebase.FireBaseRealtimeDatabaseImp
import com.repzone.mobile.firebase.FirebaseCrashServiceImp
import com.repzone.mobile.firebase.FirebasePushServiceImp
import com.repzone.mobile.manager.location.LocationServiceiOS
import com.repzone.mobile.manager.pref.IOSPreferencesManager
import org.koin.dsl.module

val IosDIModule = module {
    single<IPreferencesManager>{ IOSPreferencesManager() }
    single<ILocationService>{ LocationServiceiOS() }
}

val FirebaseIosModule = module {
    single<IFirebaseCrashService> { FirebaseCrashServiceImp() }
    single<IFireBaseRealtimeDatabase> { FireBaseRealtimeDatabaseImp() }
    single<IFirebasePushService> { FirebasePushServiceImp() }
}