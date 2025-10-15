package com.repzone.core.interfaces

interface IPreferencesManager {
    fun setUserSessions(value: String?)
    fun getUserSessions(): String?
    fun setActiveUserCode(value: Int)
    fun getActiveUserCode(): Int
}