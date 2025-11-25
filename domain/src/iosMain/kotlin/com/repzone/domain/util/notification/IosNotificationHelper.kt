package com.repzone.domain.util.notification

import com.repzone.core.platform.Logger

class IosNotificationHelper: IPlatformNotificationHelper {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun showGpsDisabledNotification() {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: GPS Disabled notification")
    }

    override suspend fun dismissGpsDisabledNotification() {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: Dismiss GPS Disabled notification")
    }

    override suspend fun showScheduleStartingNotification(minutesUntilStart: Int) {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: Schedule starting in $minutesUntilStart minutes")
    }

    override suspend fun showScheduleEndingNotification(minutesUntilEnd: Int) {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: Schedule ending in $minutesUntilEnd minutes")
    }

    override suspend fun showBatteryLowNotification(batteryLevel: Int, newInterval: Int) {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: Battery low $batteryLevel% - interval changed to $newInterval min")
    }

    override suspend fun showSyncPendingNotification(pendingCount: Int) {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: $pendingCount locations pending sync")
    }

    override suspend fun dismissAllTrackingNotifications() {
        // TODO: UNUserNotificationCenter implementation
        Logger.d("iOS: Dismiss all tracking notifications")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}