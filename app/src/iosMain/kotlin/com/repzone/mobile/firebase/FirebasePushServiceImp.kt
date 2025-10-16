package com.repzone.mobile.firebase

import com.repzone.core.interfaces.IFirebasePushService

class FirebasePushServiceImp: IFirebasePushService {
    //region Public Method
    //TODO( SwiftPM ile platform özelinde eklenmesi gerekmektedir. )
    override suspend fun getToken(): String? {
        // FCM token, APNs token ile ilişkilendirilir; iOS’ta bildirim iznini ve
        // APNs token register’ını AppDelegate tarafında yapmalısın.
        //return awaitFcmTokenFromMessaging()
        return null
    }

    /*private suspend fun awaitFcmTokenFromMessaging(): String? = suspendCancellableCoroutine { cont ->
        Messaging.messaging().tokenWithCompletion { token, error ->
            if (error != null) cont.resume(null) {} else cont.resume(token) {}
        }
    }*/
    //endregion

}