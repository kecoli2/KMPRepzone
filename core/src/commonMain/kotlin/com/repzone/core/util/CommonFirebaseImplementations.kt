package com.repzone.core.util

import kotlinx.coroutines.flow.Flow

interface IFirebaseCrashService {
    fun log(message: String)
    fun record(throwable: Throwable)
}

interface IFirebasePushService {
    suspend fun getToken(): String?
}

interface IFireBaseRealtimeDatabase {
    suspend fun set(path: String, value: String?)
    fun observe(path: String): Flow<String>
}