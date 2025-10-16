package com.repzone.mobile.firebase

import com.repzone.core.interfaces.IFirebasePushService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebasePushServiceImp: IFirebasePushService {
    //region Public Method
    override suspend fun getToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }
    //endregion


}