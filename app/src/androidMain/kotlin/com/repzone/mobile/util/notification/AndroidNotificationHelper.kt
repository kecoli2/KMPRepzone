package com.repzone.mobile.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.repzone.domain.util.notification.IPlatformNotificationHelper
import com.repzone.domain.util.notification.NotificationIds
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*
import org.jetbrains.compose.resources.getString

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
    override suspend fun showGpsDisabledNotification() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val sss = getString(Res.string.note)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(getString(Res.string.notification_gps_disabled_title))
            .setContentText(getString(Res.string.notification_gps_disabled_text))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(Res.string.notification_gps_disabled_bigtext)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_preferences, getString(Res.string.action_open_settings), pendingIntent)
            .build()

        notificationManager.notify(NotificationIds.GPS_DISABLED, notification)
    }

    override suspend fun dismissGpsDisabledNotification() {
        notificationManager.cancel(NotificationIds.GPS_DISABLED)
    }

    override suspend fun showScheduleStartingNotification(minutesUntilStart: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle(getString(Res.string.notification_schedule_starting_title))
            .setContentText(getString(Res.string.notification_schedule_starting_text, minutesUntilStart))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SCHEDULE_STARTING, notification)
    }

    override suspend fun showScheduleEndingNotification(minutesUntilEnd: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle(getString(Res.string.notification_schedule_ending_title))
            .setContentText(getString(Res.string.notification_schedule_ending_text, minutesUntilEnd))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SCHEDULE_ENDING, notification)
    }

    override suspend fun showBatteryLowNotification(batteryLevel: Int, newInterval: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(getString(Res.string.notification_battery_low_title))
            .setContentText(getString(Res.string.notification_battery_low_text, batteryLevel, newInterval))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(Res.string.notification_battery_low_bigtext, batteryLevel, newInterval)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.BATTERY_LOW, notification)
    }

    override suspend fun showSyncPendingNotification(pendingCount: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(getString(Res.string.notification_sync_pending_title))
            .setContentText(getString(Res.string.notification_sync_pending_text, pendingCount))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(Res.string.notification_sync_pending_bigtext, pendingCount) ))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NotificationIds.SYNC_PENDING, notification)
    }

    override suspend fun dismissAllTrackingNotifications() {
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