package com.repzone.core.interfaces

interface ITokenProvider {
    /** Şu anki access token (yoksa null) */
    fun getToken(): String?

    /** Access token + expire bilgisini günceller (ör. login sonrasında) */
    suspend fun setToken(token: String, expiresAtEpochSeconds: Long?, refreshToken: String?)

    /** Gerekiyorsa (expired/401) token yeniler; başarı → true */
    suspend fun refreshToken(): Boolean

    /** Logout */
    suspend fun clear()

    /** Expire kontrolü (clock skew için küçük bir pay bırakabilirsiniz) */
    fun isExpired(graceSeconds: Long = 15): Boolean
}