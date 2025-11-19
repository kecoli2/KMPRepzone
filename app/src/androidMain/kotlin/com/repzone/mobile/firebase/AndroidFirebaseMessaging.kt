package com.repzone.mobile.firebase

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.repzone.domain.common.DomainException
import com.repzone.domain.firebase.IFirebaseMessaging
import kotlinx.coroutines.tasks.await
import com.repzone.domain.common.Result

class AndroidFirebaseMessaging : IFirebaseMessaging {
    private val messaging get() = Firebase.messaging

    override suspend fun getToken(): Result<String> {
        return try {
            val token = messaging.token.await()
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun subscribeToTopic(topic: String): Result<Unit> {
        return try {
            messaging.subscribeToTopic(topic).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun unsubscribeFromTopic(topic: String): Result<Unit> {
        return try {
            messaging.unsubscribeFromTopic(topic).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun hasNotificationPermission(): Boolean {
        return true // Android için simplified
    }

    override suspend fun requestNotificationPermission(): Result<Boolean> {
        return Result.Success(true) // Activity'den handle edilmeli
    }
}