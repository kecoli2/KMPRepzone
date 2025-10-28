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
    override fun setUserSessions(value: String?) {
        if(value == null){
            prefs.edit { remove("${PreferencesConstant.USER_SESSIONS}_${getActiveUserCode()}").apply() }
        }else{
            prefs.edit { putString("${PreferencesConstant.USER_SESSIONS}_${getActiveUserCode()}", value).apply() }
        }
    }

    override fun getUserSessions(): String? {
        return prefs.getString("${PreferencesConstant.USER_SESSIONS}_${getActiveUserCode()}", null)
    }

    override fun setActiveUserCode(value: Int) {
        prefs.edit { putInt(PreferencesConstant.ACTIVE_USER_CODE, value).apply() }
    }

    override fun getActiveUserCode(): Int {
        return prefs.getInt(PreferencesConstant.ACTIVE_USER_CODE, 0)
    }

    override fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun setBooleanValue(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value).apply() }
    }

    override fun getIntValue(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    override fun setIntValue(key: String, value: Int) {
        prefs.edit { putInt(key, value).apply() }
    }

    override fun getStringValue(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    override fun setStringValue(key: String, value: String?) {
        prefs.edit { putString(key, value).apply() }
    }

    override fun reoveKey(key: String) {
        prefs.edit { remove(key).apply() }
    }
    //endregion

}