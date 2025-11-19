package com.repzone.mobile.firebase

import com.repzone.core.platform.Logger
import com.repzone.domain.firebase.IFirebaseCrashlytics

class IOSFirebaseCrashlytics : IFirebaseCrashlytics {

    // TODO: import cocoapods.FirebaseCrashlytics.*

    override fun initialize() {
        // iOS'ta otomatik başlar
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics initialized")
    }

    override fun recordException(exception: Throwable) {
        // TODO: FIRCrashlytics.crashlytics().record(error: exception)
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics: recordException not yet implemented - ${exception.message}")
    }

    override fun log(message: String) {
        // TODO: FIRCrashlytics.crashlytics().log(message)
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics Log: $message")
    }

    override fun setUserId(userId: String) {
        // TODO: FIRCrashlytics.crashlytics().setUserID(userId)
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics: setUserId - $userId")

    }

    override fun setCustomKey(key: String, value: String) {
        // TODO: FIRCrashlytics.crashlytics().setCustomValue(value, forKey: key)
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics: setCustomKey - $key: $value")
    }

    override fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        // TODO: FIRCrashlytics.crashlytics().setCrashlyticsCollectionEnabled(enabled)
        Logger.d ("IOSFirebaseCrashlytics","iOS Crashlytics: collection ${if (enabled) "enabled" else "disabled"}")
    }
}