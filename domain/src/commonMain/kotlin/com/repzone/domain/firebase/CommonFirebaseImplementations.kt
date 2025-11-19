package com.repzone.domain.firebase

import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsLocation

interface IFirebaseCrashlytics {
    fun initialize()
    fun recordException(exception: Throwable)
    fun log(message: String)
    fun setUserId(userId: String)
    fun setCustomKey(key: String, value: String)
    fun setCrashlyticsCollectionEnabled(enabled: Boolean)
}

interface IFirebaseMessaging  {
    suspend fun getToken(): Result<String>
    suspend fun subscribeToTopic(topic: String): Result<Unit>
    suspend fun unsubscribeFromTopic(topic: String): Result<Unit>
    suspend fun hasNotificationPermission(): Boolean
    suspend fun requestNotificationPermission(): Result<Boolean>
}

interface IFirebaseRealtimeDatabase {
    suspend fun writeData(path: String, data: Map<String, Any>): Result<Unit>
    suspend fun readData(path: String): Result<Map<String, Any>?>
    suspend fun updateData(path: String, updates: Map<String, Any>): Result<Unit>
    suspend fun deleteData(path: String): Result<Unit>
    suspend fun sendToFirebase(data: GpsLocation): Result<Boolean>
    fun listenToData(path: String, onDataChange: (Map<String, Any>?) -> Unit)
    fun stopListening(path: String)
    suspend fun userAuthentication(email: String): Result<Unit>
}