package com.repzone.mobile.firebase

import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.domain.common.DomainException
import com.repzone.domain.firebase.IFirebaseMessaging
import com.repzone.domain.common.Result

class IOSFirebaseMessaging : IFirebaseMessaging {

    // TODO: import cocoapods.FirebaseMessaging.*

    override suspend fun getToken(): Result<String> {
        return try {
            // TODO: suspendCancellableCoroutine { continuation ->
            //     FIRMessaging.messaging().token { token, error ->
            //         if (let error = error) {
            //             continuation.resume(Result.failure(Exception(error.localizedDescription)))
            //         } else {
            //             continuation.resume(Result.success(token ?: ""))
            //         }
            //     }
            // }
            Logger.d("IOSFirebaseMessaging","iOS Messaging: getToken not yet implemented")
            Result.Success("ios_mock_token_${now()}")
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun subscribeToTopic(topic: String): Result<Unit> {
        return try {
            // TODO: FIRMessaging.messaging().subscribe(toTopic: topic)
            Logger.d("IOSFirebaseMessaging","iOS Messaging: subscribeToTopic '$topic' not yet implemented")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun unsubscribeFromTopic(topic: String): Result<Unit> {
        return try {
            // TODO: FIRMessaging.messaging().unsubscribe(fromTopic: topic)
            Logger.d("IOSFirebaseMessaging","iOS Messaging: unsubscribeFromTopic '$topic' not yet implemented")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun hasNotificationPermission(): Boolean {
        // TODO: UNUserNotificationCenter.current().getNotificationSettings()
        Logger.d("IOSFirebaseMessaging","iOS Messaging: hasNotificationPermission not yet implemented")
        return false
    }

    override suspend fun requestNotificationPermission(): Result<Boolean> {
        return try {
            // TODO: UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge])
            Logger.d("IOSFirebaseMessaging","iOS Messaging: requestNotificationPermission not yet implemented")
            Result.Success(false)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }
}