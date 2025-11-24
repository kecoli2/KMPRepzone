package com.repzone.domain.util.notification

interface IPlatformNotificationHelper {
    /**
     * GPS kapalı bildirimi göster
     */
    suspend fun showGpsDisabledNotification()

    /**
     * GPS kapalı bildirimini kaldır
     */
    suspend fun dismissGpsDisabledNotification()

    /**
     * Schedule başlangıç bildirimi
     */
    suspend fun showScheduleStartingNotification(minutesUntilStart: Int)

    /**
     * Schedule bitiş bildirimi
     */
    suspend fun showScheduleEndingNotification(minutesUntilEnd: Int)

    /**
     * Batarya düşük bildirimi
     */
    suspend fun showBatteryLowNotification(batteryLevel: Int, newInterval: Int)

    /**
     * Network yok - sync bekliyor bildirimi
     */
    suspend fun showSyncPendingNotification(pendingCount: Int)

    /**
     * Tüm tracking bildirimlerini kaldır (GPS kapalı hariç)
     */
    suspend fun dismissAllTrackingNotifications()
}