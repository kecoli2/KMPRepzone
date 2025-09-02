package com.repzone.firebase.manager

import com.repzone.core.interfaces.IFirebaseCrashService
import platform.Foundation.NSLog

class FirebaseCrashServiceImp: IFirebaseCrashService {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    //TODO( SwiftPM ile platform özelinde eklenmesi gerekmektedir. )
    override fun log(message: String) {
        NSLog(message)
        //FIRCrashlytics.crashlytics().log(message)
    }

    //TODO( SwiftPM ile platform özelinde eklenmesi gerekmektedir. )
    override fun record(throwable: Throwable) {
        //TODO("Not yet implemented")
        //FIRCrashlytics.crashlytics().record(error = e)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}