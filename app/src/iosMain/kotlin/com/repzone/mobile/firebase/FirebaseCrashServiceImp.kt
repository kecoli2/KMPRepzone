package com.repzone.mobile.firebase

import com.repzone.core.interfaces.IFirebaseCrashService
import platform.Foundation.NSLog

class FirebaseCrashServiceImp: IFirebaseCrashService {
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

}