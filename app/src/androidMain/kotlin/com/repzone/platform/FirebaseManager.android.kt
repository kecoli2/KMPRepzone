package com.repzone.platform

import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.mobile.firebase.AndroidFirebaseCrashlytics
import com.repzone.mobile.firebase.AndroidFirebaseMessaging
import com.repzone.mobile.firebase.AndroidFirebaseRealtimeDatabase

actual class FirebaseFactory actual constructor() {
    actual fun createRealtimeDatabase(): IFirebaseRealtimeDatabase {
        return AndroidFirebaseRealtimeDatabase()
    }

    actual fun createCrashlytics(): IFirebaseCrashlytics {
        return AndroidFirebaseCrashlytics()
    }

    actual fun createMessaging(): IFirebaseMessaging {
        return AndroidFirebaseMessaging()
    }
}