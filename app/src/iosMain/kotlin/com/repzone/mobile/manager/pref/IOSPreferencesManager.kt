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
    override fun setUserSessions(value: String?) {
        if(value == null){
            prefs.removeObjectForKey("${PreferencesConstant.USER_SESSIONS}_${getActiveUserCode()}")
        }else{
            prefs.setObject(value, "${PreferencesConstant.USER_SESSIONS}_${getActiveUserCode()}")
        }
    }

    override fun getUserSessions(): String? {
        return prefs.stringForKey(PreferencesConstant.USER_SESSIONS)
    }

    override fun setActiveUserCode(value: Int) {
        prefs.setInteger(value.toLong(), PreferencesConstant.ACTIVE_USER_CODE)
    }

    override fun getActiveUserCode(): Int {
        return prefs.integerForKey(PreferencesConstant.ACTIVE_USER_CODE).toInt()
    }

    override fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return prefs.boolForKey(key)
    }

    override fun setBooleanValue(key: String, value: Boolean) {
        prefs.setBool(value, key)
    }

    override fun getIntValue(key: String, defaultValue: Int): Int {
        return prefs.integerForKey(key).toInt()
    }

    override fun setIntValue(key: String, value: Int) {
        prefs.setInteger(value.toLong(), key)
    }

    override fun getStringValue(key: String, defaultValue: String?): String? {
        return prefs.stringForKey(key)
    }

    override fun setStringValue(key: String, value: String?) {
        prefs.setObject(value, key)
    }

    override fun reoveKey(key: String) {
        prefs.removeObjectForKey(key)
    }
    //endregion


}