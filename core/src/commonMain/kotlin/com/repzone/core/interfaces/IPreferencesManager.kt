package com.repzone.core.interfaces

interface IPreferencesManager {
    fun getToken(): String?
    fun setToken(token: String?)
}