package com.repzone.platform

import com.repzone.core.interfaces.IFirebaseManager
import com.repzone.domain.firebase.IFirebaseCrashlytics
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase

class FirebaseManager(val realtimeDatabase: IFirebaseRealtimeDatabase,
                      val crashlytics: IFirebaseCrashlytics, val messaging: IFirebaseMessaging): IFirebaseManager {

    //region Public Method
    override suspend fun initialize(userId: String, userName: String?, email: String) {
        crashlytics.initialize()
        setUser(userId, userName)
        userAuthentication(email)
    }

    //endregion

    //region Private Method
    private fun setUser(userId: String, userName: String?){
        crashlytics.setUserId(userId)
        userName?.let {
            crashlytics.setCustomKey("user_name", it)
        }
    }

    private suspend fun userAuthentication(email: String): Boolean {
        return realtimeDatabase.userAuthentication(email).isSuccess
    }
    //endregion

    //region Protected Method
    //endregion
}