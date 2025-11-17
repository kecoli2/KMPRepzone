package com.repzone.mobile.platform

import android.content.Context
import com.repzone.domain.platform.IPlatformServiceController
import com.repzone.mobile.service.GpsTrackingService

class AndroidServiceController(private val context: Context) : IPlatformServiceController {
    //region Public Method
    override fun startForegroundService() {
        GpsTrackingService.startService(context)
    }

    override fun stopForegroundService() {
        GpsTrackingService.stopService(context)
    }

    override fun isServiceRunning(): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (GpsTrackingService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
    //endregion
}