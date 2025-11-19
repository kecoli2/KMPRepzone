package com.repzone.platform

import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase

class FirebaseManager(val realtimeDatabase: IFirebaseRealtimeDatabase, val crashlytics: IFirebaseCrashlytics, val messaging: IFirebaseMessaging) {

    //region Constructor
    fun initialize() {
        crashlytics.initialize()
    }
    //endregion

    //region Public Method
    fun setUser(userId: String, userName: String? = null){
        crashlytics.setUserId(userId)
        userName?.let {
            crashlytics.setCustomKey("user_name", it)
        }
    }
    //endregion

    //region Protected Method
    //endregion
}

expect class FirebaseFactory() {
    fun createRealtimeDatabase(): IFirebaseRealtimeDatabase
    fun createCrashlytics(): IFirebaseCrashlytics
    fun createMessaging(): IFirebaseMessaging
}

fun createFirebaseManager(): FirebaseManager {
    val factory = FirebaseFactory()
    return FirebaseManager(
        realtimeDatabase = factory.createRealtimeDatabase(),
        crashlytics = factory.createCrashlytics(),
        messaging = factory.createMessaging()
    )
}