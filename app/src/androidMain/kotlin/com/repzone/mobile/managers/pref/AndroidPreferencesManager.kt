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
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}