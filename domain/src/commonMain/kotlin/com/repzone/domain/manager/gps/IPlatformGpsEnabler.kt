package com.repzone.domain.manager.gps

interface IPlatformGpsEnabler {
    suspend fun requestEnableGps(): Boolean
}