package com.repzone.core.model

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.jsonToModel
import com.repzone.core.util.extensions.toJson

class UserSessionImp(private val preferences: IPreferencesManager): IUserSession {
    //region Field
    private var activeSessionModel: UserSessionModel? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun clearSession() {
        activeSessionModel = null
    }

    override fun getActiveSession(): UserSessionModel? {
        if(activeSessionModel == null){
            activeSessionModel = preferences.getUserSessions()?.jsonToModel<UserSessionModel>()
            return activeSessionModel
        }
        return activeSessionModel
    }

    override fun loadActiveSession() {
        activeSessionModel = preferences.getUserSessions()?.jsonToModel<UserSessionModel>()
    }

    override fun save() {
        preferences.setUserSessions(activeSessionModel?.toJson())
    }

    override fun getReloadParameters(): Boolean {
        var reload = false
        getActiveSession()?.let {
            reload = preferences.getBooleanValue("${it.userId}_reloadParameters", false)
            if(reload){
                preferences.setBooleanValue("${it.userId}_reloadParameters", false)
            }
        }
        return reload
    }


    //endregion

}