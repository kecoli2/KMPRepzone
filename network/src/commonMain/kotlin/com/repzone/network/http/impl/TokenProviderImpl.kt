package com.repzone.network.http.impl

import com.repzone.core.interfaces.ITokenProvider
import com.repzone.core.interfaces.IUserSession
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class TokenProviderImpl(private val userSession: IUserSession) :
    ITokenProvider {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getToken(): String? {
        return userSession.getActiveSession()?.token
    }

    override suspend fun setToken(token: String, expiresAtEpochSeconds: Long?, refreshToken: String?) {
        userSession.getActiveSession()?.token = token
        userSession.getActiveSession()?.expiresAtEpochSeconds = expiresAtEpochSeconds
        userSession.getActiveSession()?.refreshToken = refreshToken
        userSession.save()
    }

    override suspend fun refreshToken(): Boolean {
        return true
        /*// Zaten geçerli ise yenileme yapmayın
        if (!isExpired()) return true

        val refresh = iPreferencesManager.getToken() ?: return false
        return try {
            val resp = iTokenApiController.refreshToken(RefreshTokenRequest(refresh))
            resp.



            // Örn. backend accessToken, expiresIn, refreshToken döndürüyor:
            val newAccess = resp
            val expiresAt = max(clock() + resp.expiresInSeconds, clock() + 30) // min 30sn
            val newRefresh = resp.refreshToken ?: refresh

            iPreferencesManager.setToken(token)
            iPreferencesManager.setExpiresAtEpochSeconds(expiresAtEpochSeconds)
            iPreferencesManager.setRefreshToken(refreshToken)


            settings.putString(KEY_ACCESS_TOKEN, newAccess)
            settings.putLong(KEY_EXPIRES_AT, expiresAt)
            settings.putString(KEY_REFRESH_TOKEN, newRefresh)
            true
        } catch (_: Throwable) {
            false
        }*/
    }

    override suspend fun clear() {
        userSession.getActiveSession()?.token = null
        userSession.getActiveSession()?.expiresAtEpochSeconds = null
        userSession.getActiveSession()?.refreshToken = null
        userSession.save()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}