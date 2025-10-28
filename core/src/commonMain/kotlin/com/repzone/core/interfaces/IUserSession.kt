package com.repzone.core.interfaces

import com.repzone.core.enums.ThemeMode
import com.repzone.core.enums.ThemeType
import com.repzone.core.model.UserSessionModel

interface IUserSession {
    fun clearSession()
    fun getActiveSession(): UserSessionModel? = null
    fun loadActiveSession()
    fun save()
    fun getReloadParameters(): Boolean
    fun setReloadParameters()

    fun saveThemeMode(mode: ThemeMode)
    fun getThemeMode(): ThemeMode?

    fun saveColorScheme(themeType: ThemeType)
    fun getColorScheme(): ThemeType?

    fun saveLanguage(languageCode: String)
    fun getLanguage(): String?
}