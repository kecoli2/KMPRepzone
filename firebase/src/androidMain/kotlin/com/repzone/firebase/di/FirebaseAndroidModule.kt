package com.repzone.firebase.di

import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import com.repzone.core.interfaces.IFirebaseCrashService
import com.repzone.core.interfaces.IFirebasePushService
import com.repzone.firebase.manager.FireBaseRealtimeDatabaseImp
import com.repzone.firebase.manager.FirebaseCrashServiceImp
import com.repzone.firebase.manager.FirebasePushServiceImp
import org.koin.dsl.module

//region Field

val FirebaseAndroidModule = module {
    single<IFirebaseCrashService> { FirebaseCrashServiceImp() }
    single<IFireBaseRealtimeDatabase> { FireBaseRealtimeDatabaseImp() }
    single<IFirebasePushService> { FirebasePushServiceImp() }
}
//endregion
