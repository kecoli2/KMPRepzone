package com.repzone.network.http.impl

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.ITokenProvider
import com.repzone.network.api.ITokenApiController
import com.repzone.network.models.request.RefreshTokenRequest
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class TokenProviderImpl(private val iPreferencesManager: IPreferencesManager,
                        private val iTokenApiController: ITokenApiController,
                        val clock: () -> Long = { Clock.System.now().toEpochMilliseconds() / 1000 }) :
    ITokenProvider {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getToken(): String? {
        return iPreferencesManager.getToken()
    }

    override suspend fun setToken(token: String, expiresAtEpochSeconds: Long?, refreshToken: String?) {
        iPreferencesManager.setToken(token)
        iPreferencesManager.setExpiresAtEpochSeconds(expiresAtEpochSeconds)
        iPreferencesManager.setRefreshToken(refreshToken)
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
        iPreferencesManager.setToken(null)
        iPreferencesManager.setRefreshToken(null)
        iPreferencesManager.setExpiresAtEpochSeconds(null)
    }

    override fun isExpired(graceSeconds: Long): Boolean {
        val exp = iPreferencesManager.getExpiresAtEpochSeconds() ?: 0
        if(exp == 0.toLong())
            return true
        return (clock() + graceSeconds) >= exp
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}