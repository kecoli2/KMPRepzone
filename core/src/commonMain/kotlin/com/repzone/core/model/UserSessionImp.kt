package com.repzone.core.model

import com.repzone.core.enums.CustomerOrRepresentativeOrganizationSelection
import com.repzone.core.enums.ThemeMode
import com.repzone.core.enums.ThemeType
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.jsonToModel
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toJson

class UserSessionImp(private val preferences: IPreferencesManager): IUserSession {
    //region Field
    private var activeSessionModel: UserSessionModel? = null
    //endregion

    //region Properties
    companion object {
        const val THEME_MODE_KEY = "theme_mode"
        const val COLOR_SCHEME_KEY = "color_scheme"
        const val LANGUAGE_KEY = "language"
    }
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

    override fun setReloadParameters(){
        getActiveSession()?.let {
            preferences.setBooleanValue("${it.userId}_reloadParameters", true)
        }
    }

    override fun saveThemeMode(mode: ThemeMode) {
        preferences.setIntValue(getUserKeyPrefId(THEME_MODE_KEY), mode.ordinal)
    }

    override fun getThemeMode(): ThemeMode? {
        val mode = preferences.getIntValue(getUserKeyPrefId(THEME_MODE_KEY), ThemeMode.SYSTEM.ordinal).toEnum<ThemeMode>()
        return mode
    }

    override fun saveColorScheme(themeType: ThemeType) {
        preferences.setIntValue(getUserKeyPrefId(COLOR_SCHEME_KEY), themeType.ordinal)
    }

    override fun getColorScheme(): ThemeType? {
        val mode = preferences.getIntValue(getUserKeyPrefId(COLOR_SCHEME_KEY), ThemeType.DEFAULT.ordinal).toEnum<ThemeType>()
        return mode
    }

    override fun saveLanguage(languageCode: String) {
        preferences.setStringValue(getUserKeyPrefId(LANGUAGE_KEY), languageCode)
    }

    override fun getLanguage(): String? {
        return preferences.getStringValue(getUserKeyPrefId(LANGUAGE_KEY), "en")
    }

    override fun decideWhichOrgIdToBeUsed(customerOrgId: Int): Int {
        getActiveSession()?.identity?.let {
            if(it.organizationSelection == CustomerOrRepresentativeOrganizationSelection.REPRESENTATIVEORGANIZATION){
                return it.organizationId
            }
        }
        return customerOrgId
    }


    //endregion

    //region Private Method
    private fun getUserKeyPrefId(value : String): String{
        return "${getActiveSession()?.userId ?: 0}_${value}"
    }
    //endregion Private Method

}