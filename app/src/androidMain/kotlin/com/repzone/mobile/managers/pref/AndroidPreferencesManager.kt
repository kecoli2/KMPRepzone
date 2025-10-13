package com.repzone.mobile.managers.pref

import android.content.Context
import com.repzone.core.constant.PreferencesConstant
import com.repzone.core.interfaces.IPreferencesManager
import androidx.core.content.edit

class AndroidPreferencesManager(private val context: Context) : IPreferencesManager {
    //region Field
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getToken(): String? {
        return prefs.getString(PreferencesConstant.TOKEN, null)
    }

    override fun setToken(token: String?) {
        if (token == null) {
            prefs.edit { remove(PreferencesConstant.TOKEN) }
        }else{
            prefs.edit { putString(PreferencesConstant.TOKEN, token) }
        }
    }

    override fun setExpiresAtEpochSeconds(expiresAtEpochSeconds: Long?) {
        if(expiresAtEpochSeconds == null){
            prefs.edit { remove(PreferencesConstant.TOKEN_EXPIRES_AT) }
        }else{
            prefs.edit { putLong(PreferencesConstant.TOKEN_EXPIRES_AT, expiresAtEpochSeconds) }
        }
    }

    override fun getExpiresAtEpochSeconds(): Long? {
        return prefs.getLong(PreferencesConstant.TOKEN_EXPIRES_AT, 0)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(PreferencesConstant.REFRESH_TOKEN, null)
    }

    override fun setRefreshToken(token: String?) {
        if (token == null) {
            prefs.edit { remove(PreferencesConstant.REFRESH_TOKEN) }
        }else{
            prefs.edit { putString(PreferencesConstant.REFRESH_TOKEN, token) }
        }
    }

    override fun setUserSessions(value: String?) {
        if(value == null){
            prefs.edit { remove(PreferencesConstant.USER_SESSIONS) }
        }else{
            prefs.edit { putString(PreferencesConstant.USER_SESSIONS, value) }
        }
    }

    override fun getUserSessions(): String? {
        return prefs.getString(PreferencesConstant.USER_SESSIONS, null)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}