package com.repzone.core.interfaces

import com.repzone.core.model.UserSessionModel

interface IUserSession {
    fun clearSession()
    fun getActiveSession(): UserSessionModel? = null
    fun loadActiveSession()
    fun save()

    fun getReloadParameters(): Boolean
}