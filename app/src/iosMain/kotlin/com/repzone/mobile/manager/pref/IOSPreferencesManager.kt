package com.repzone.mobile.manager.pref

import com.repzone.core.constant.PreferencesConstant
import com.repzone.core.interfaces.IPreferencesManager
import platform.Foundation.NSUserDefaults

class IOSPreferencesManager: IPreferencesManager {
    //region Field
    private val prefs = NSUserDefaults.standardUserDefaults
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getToken(): String? {
        return prefs.stringForKey(PreferencesConstant.TOKEN)
    }

    override fun setToken(token: String?) {
        if (token == null) {
            prefs.removeObjectForKey(PreferencesConstant.TOKEN)
        }else{
            prefs.setObject(token, PreferencesConstant.TOKEN)
        }
    }

    override fun setExpiresAtEpochSeconds(expiresAtEpochSeconds: Long?) {
        if(expiresAtEpochSeconds == null){
            prefs.removeObjectForKey(PreferencesConstant.TOKEN_EXPIRES_AT)
        }else{
            prefs.setInteger(expiresAtEpochSeconds, PreferencesConstant.TOKEN_EXPIRES_AT)
        }
    }

    override fun getExpiresAtEpochSeconds(): Long? {
        return prefs.integerForKey(PreferencesConstant.TOKEN_EXPIRES_AT)
    }

    override fun getRefreshToken(): String? {
        return prefs.stringForKey(PreferencesConstant.REFRESH_TOKEN)
    }

    override fun setRefreshToken(token: String?) {
        if (token == null) {
            prefs.removeObjectForKey(PreferencesConstant.REFRESH_TOKEN)
        }else{
            prefs.setObject(token, PreferencesConstant.REFRESH_TOKEN)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}