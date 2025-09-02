package com.repzone.firebase.manager

import com.repzone.core.util.IFirebasePushService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebasePushServiceImp: IFirebasePushService {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}