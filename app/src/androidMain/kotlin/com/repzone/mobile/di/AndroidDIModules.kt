package com.repzone.mobile.di

import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import com.repzone.core.interfaces.IFirebaseCrashService
import com.repzone.core.interfaces.IFirebasePushService
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.mobile.firebase.FireBaseRealtimeDatabaseImp
import com.repzone.mobile.firebase.FireBaseRealtimeDatabaseImpFake
import com.repzone.mobile.firebase.FirebaseCrashServiceImp
import com.repzone.mobile.firebase.FirebasePushServiceImp
import com.repzone.mobile.managers.pref.AndroidPreferencesManager
import com.repzone.mobile.managers.pref.AndroidPreferencesManagerPreview
import org.koin.dsl.module

val AndroidDIModule = module {
    single<IPreferencesManager>{ AndroidPreferencesManager(context = get()) }
}

val AndroidDIModulePreview = module {
    single<IPreferencesManager>{ AndroidPreferencesManagerPreview() }
}

val FirebaseAndroidModule = module {
    single<IFirebaseCrashService> { FirebaseCrashServiceImp() }
    single<IFireBaseRealtimeDatabase> { FireBaseRealtimeDatabaseImp() }
    single<IFirebasePushService> { FirebasePushServiceImp() }
}

val FirebaseMockAndroidModule = module {
    single<IFireBaseRealtimeDatabase> { FireBaseRealtimeDatabaseImpFake() }
    single<IFirebasePushService> { FirebasePushServiceImp() }
}