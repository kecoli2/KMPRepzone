package com.repzone.core.interfaces

interface ITokenProvider {
    fun getToken(): String?
    suspend fun setToken(token: String, expiresAtEpochSeconds: Long?, refreshToken: String?)
    suspend fun refreshToken(): Boolean
    suspend fun clear()
}