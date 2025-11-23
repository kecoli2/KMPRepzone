package com.repzone.domain.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.repzone.domain.util.IPlatformNotificationHelper

class AndroidNotificationHelper(private val context: Context): IPlatformNotificationHelper {
    //region Field
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "gps_tracking_alerts"
        private const val CHANNEL_NAME = "GPS Tracking Alerts"
    }

    //endregion

    //region Properties
    //endregion

    //region Constructor
    init {
        createNotificationChannel()
    }
    //endregion

    //region Public Method
    override fun showGpsDisabledNotification() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("GPS Kapalı")
            .setContentText("GPS tracking için konum servislerini açmalısınız")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("GPS tracking çalışabilmesi için cihazınızın konum servislerini (GPS) açmalısınız. Ayarlara gitmek için tıklayın."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_preferences, "Ayarları Aç", pendingIntent)
            .build()

        notificationManager.notify(NotificationIds.GPS_DISABLED, notification)
    }

    override fun dismissGpsDisabledNotification() {
        notificationManager.cancel(NotificationIds.GPS_DISABLED)
    }

    override fun showScheduleStartingNotification(minutesUntilStart: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle("⏰ GPS Tracking Başlayacak")
            .setContentText("$minutesUntilStart dakika sonra GPS tracking başlayacak")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SCHEDULE_STARTING, notification)
    }

    override fun showScheduleEndingNotification(minutesUntilEnd: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle("⏰ GPS Tracking Duracak")
            .setContentText("$minutesUntilEnd dakika sonra GPS tracking duracak")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SCHEDULE_ENDING, notification)
    }

    override fun showBatteryLowNotification(batteryLevel: Int, newInterval: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔋 Batarya Düşük")
            .setContentText("Batarya %$batteryLevel - GPS aralığı $newInterval dakikaya çıkarıldı")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Batarya seviyeniz düşük (%$batteryLevel). Batarya tasarrufu için GPS tracking aralığı $newInterval dakikaya çıkarıldı."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.BATTERY_LOW, notification)
    }

    override fun showSyncPendingNotification(pendingCount: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("📤 Senkronizasyon Bekliyor")
            .setContentText("$pendingCount konum senkronize edilmeyi bekliyor")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$pendingCount konum senkronize edilmeyi bekliyor. WiFi'ye bağlandığınızda otomatik olarak gönderilecek."))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SYNC_PENDING, notification)
    }

    override fun dismissAllTrackingNotifications() {
        notificationManager.cancel(NotificationIds.SCHEDULE_STARTING)
        notificationManager.cancel(NotificationIds.SCHEDULE_ENDING)
        notificationManager.cancel(NotificationIds.BATTERY_LOW)
        notificationManager.cancel(NotificationIds.SYNC_PENDING)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "GPS tracking alerts and notifications"
                setShowBadge(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}