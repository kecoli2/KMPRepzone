package com.repzone.mobile.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.repzone.core.interfaces.IFirebaseCrashService

class FirebaseCrashServiceImp: IFirebaseCrashService {
    //region Public Method
    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun record(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
    //endregion

}
