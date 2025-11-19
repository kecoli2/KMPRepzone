package com.repzone.core.interfaces

import com.repzone.core.enums.PlatformType

interface IDeviceInfo {
    fun getBatteryLevel(): Int?
    fun getDeviceModel(): String
    fun getOsVersion(): String
    fun getDeviceName(): String
    fun getPlatformType(): PlatformType
    fun getAppVersion(): String
}