package com.repzone.mobile.impl

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import com.repzone.core.config.BuildConfig
import com.repzone.core.enums.PlatformType
import com.repzone.core.interfaces.IDeviceInfo

class DeviceInfoImpl(private val context: Context) : IDeviceInfo {
    override fun getBatteryLevel(): Int? {
        return try {
            val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
                context.registerReceiver(null, filter)
            }

            batteryStatus?.let { intent ->
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                if (level != -1 && scale != -1) {
                    (level.toFloat() / scale.toFloat() * 100).toInt()
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    override fun getOsVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    override fun getDeviceName(): String {
        return Build.DEVICE
    }

    override fun getPlatformType(): PlatformType {
        return PlatformType.ANDROID
    }

    override fun getAppVersion(): String {
        return BuildConfig.APP_VERSION
    }
}