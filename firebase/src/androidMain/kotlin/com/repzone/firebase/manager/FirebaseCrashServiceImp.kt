package com.repzone.firebase.manager

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.repzone.core.interfaces.IFirebaseCrashService

class FirebaseCrashServiceImp: IFirebaseCrashService {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun record(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}

