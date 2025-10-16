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
    //endregion


}