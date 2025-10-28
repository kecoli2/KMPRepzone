package com.repzone.core.interfaces

interface IPreferencesManager {
    fun setUserSessions(value: String?)
    fun getUserSessions(): String?
    fun setActiveUserCode(value: Int)
    fun getActiveUserCode(): Int
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean
    fun setBooleanValue(key: String, value: Boolean)
    fun getIntValue(key: String, defaultValue: Int): Int
    fun setIntValue(key: String, value: Int)
    fun getStringValue(key: String, defaultValue: String?): String?
    fun setStringValue(key: String, value: String?)

    fun reoveKey(key: String)

}