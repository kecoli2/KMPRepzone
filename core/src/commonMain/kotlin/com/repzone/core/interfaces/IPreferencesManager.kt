package com.repzone.core.interfaces

interface IPreferencesManager {
    fun getToken(): String?
    fun setToken(token: String?)

    fun setExpiresAtEpochSeconds(expiresAtEpochSeconds: Long?)
    fun getExpiresAtEpochSeconds():Long?
    fun getRefreshToken(): String?
    fun setRefreshToken(token: String?)
}