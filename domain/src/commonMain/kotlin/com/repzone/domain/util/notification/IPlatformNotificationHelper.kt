package com.repzone.domain.util

interface IPlatformNotificationHelper {
    /**
     * GPS kapalı bildirimi göster
     */
    fun showGpsDisabledNotification()

    /**
     * GPS kapalı bildirimini kaldır
     */
    fun dismissGpsDisabledNotification()

    /**
     * Schedule başlangıç bildirimi
     */
    fun showScheduleStartingNotification(minutesUntilStart: Int)

    /**
     * Schedule bitiş bildirimi
     */
    fun showScheduleEndingNotification(minutesUntilEnd: Int)

    /**
     * Batarya düşük bildirimi
     */
    fun showBatteryLowNotification(batteryLevel: Int, newInterval: Int)

    /**
     * Network yok - sync bekliyor bildirimi
     */
    fun showSyncPendingNotification(pendingCount: Int)

    /**
     * Tüm tracking bildirimlerini kaldır (GPS kapalı hariç)
     */
    fun dismissAllTrackingNotifications()
}