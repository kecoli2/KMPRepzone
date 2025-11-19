package com.repzone.platform

import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase

actual class FirebaseFactory actual constructor() {
    actual fun createRealtimeDatabase(): IFirebaseRealtimeDatabase {
        TODO("Not yet implemented")
    }

    actual fun createCrashlytics(): IFirebaseCrashlytics {
        TODO("Not yet implemented")
    }

    actual fun createMessaging(): IFirebaseMessaging {
        TODO("Not yet implemented")
    }
}