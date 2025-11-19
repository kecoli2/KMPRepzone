package com.repzone.core.interfaces

interface IFirebaseManager {
    suspend fun initialize(userId: String, userName: String? = null, email: String)

}