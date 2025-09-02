package com.repzone.firebase.di

import com.repzone.core.util.IFireBaseRealtimeDatabase
import com.repzone.core.util.IFirebaseCrashService
import com.repzone.core.util.IFirebasePushService
import com.repzone.firebase.manager.FireBaseRealtimeDatabaseImp
import com.repzone.firebase.manager.FirebaseCrashServiceImp
import com.repzone.firebase.manager.FirebasePushServiceImp
import org.koin.dsl.module

//region Field
val FirebaseIosModule = module {
    single<IFirebaseCrashService> { FirebaseCrashServiceImp() }
    single<IFireBaseRealtimeDatabase> { FireBaseRealtimeDatabaseImp() }
    single<IFirebasePushService> { FirebasePushServiceImp() }
}
//endregion
