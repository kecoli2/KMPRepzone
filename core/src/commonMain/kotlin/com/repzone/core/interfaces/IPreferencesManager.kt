package com.repzone.core.interfaces

interface IPreferencesManager {
    fun setUserSessions(value: String?)
    fun getUserSessions(): String?
    fun setActiveUserCode(value: Int)
    fun getActiveUserCode(): Int
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean
    fun setBooleanValue(key: String, value: Boolean)

}