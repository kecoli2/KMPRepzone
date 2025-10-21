package com.repzone.mobile.managers.pref

import com.repzone.core.interfaces.IPreferencesManager

class AndroidPreferencesManagerPreview: IPreferencesManager {
    //region Public Method
    //endregion

    override fun setUserSessions(value: String?) {

    }

    override fun getUserSessions(): String? {
        return null
    }

    override fun setActiveUserCode(value: Int) {

    }

    override fun getActiveUserCode(): Int {
        return 0
    }

    override fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return false
    }

    override fun setBooleanValue(key: String, value: Boolean) {

    }
}