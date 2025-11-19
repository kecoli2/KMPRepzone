package com.repzone.mobile.impl

import com.repzone.core.config.BuildConfig
import com.repzone.core.enums.PlatformType
import com.repzone.core.interfaces.IDeviceInfo
import platform.UIKit.UIDevice

class IosDeviceInfo: IDeviceInfo {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getBatteryLevel(): Int? {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
        val level = UIDevice.currentDevice.batteryLevel
        return if (level >= 0) {
            (level * 100).toInt()
        } else null
    }

    override fun getDeviceModel(): String {
        return UIDevice.currentDevice.model
    }

    override fun getOsVersion(): String {
        return "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"
    }

    override fun getDeviceName(): String {
        return UIDevice.currentDevice.name
    }

    override fun getPlatformType(): PlatformType {
        return PlatformType.IOS
    }

    override fun getAppVersion(): String {
        return BuildConfig.APP_VERSION
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}