package com.repzone.core.model

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.jsonToModel

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
            val sss = preferences.getUserSessions()?.jsonToModel<UserSessionModel>()
            return sss
        }else{
            error("No active session")
        }
    }

    override fun loadActiveSession() {
        activeSessionModel = preferences.getUserSessions()?.jsonToModel<UserSessionModel>()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}