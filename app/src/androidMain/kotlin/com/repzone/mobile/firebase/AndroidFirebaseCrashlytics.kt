package com.repzone.mobile.firebase

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.repzone.domain.firebase.IFirebaseCrashlytics

class AndroidFirebaseCrashlytics : IFirebaseCrashlytics {
    private val crashlytics get() = Firebase.crashlytics

    override fun initialize() {
        // Android'de otomatik başlar
    }

    override fun recordException(exception: Throwable) {
        crashlytics.recordException(exception)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        crashlytics.isCrashlyticsCollectionEnabled = enabled
    }
}