package com.repzone.mobile.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.manager.gps.IGpsTrackingManager
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.android.ext.android.inject
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class GpsTrackingService: Service() {
    //region Field
    private val gpsTrackingManager: IGpsTrackingManager by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var isRunning = false
    //endregion

    //region Public Method
    companion object {
        const val CHANNEL_ID = "gps_tracking_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"

        fun startService(context: Context) {
            val intent = Intent(context, GpsTrackingService::class.java).apply {
                action = ACTION_START
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, GpsTrackingService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }

        fun pauseService(context: Context) {
            val intent = Intent(context, GpsTrackingService::class.java).apply {
                action = ACTION_PAUSE
            }
            context.startService(intent)
        }

        fun resumeService(context: Context) {
            val intent = Intent(context, GpsTrackingService::class.java).apply {
                action = ACTION_RESUME
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            when (intent?.action) {
                ACTION_START -> startTracking()
                ACTION_STOP -> stopTracking()
                ACTION_PAUSE -> pauseTracking()
                ACTION_RESUME -> resumeTracking()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        stopTracking()

    }
    //endregion

    //region Private Method
    @SuppressLint("ForegroundServiceType")
    private suspend fun startTracking() {
        if (isRunning) return

        // Foreground notification gösteriyor ama içeriği haluk bey ile görüş
        val notification = createNotification(
            title = getString(Res.string.gps_tracking_active),
            text = getString(Res.string.gps_tracking_pull_location_information),
            showLocation = false
        )
        startForeground(NOTIFICATION_ID, notification)

        serviceScope.launch {
            try {
                // GPS tracking'i başlat
                gpsTrackingManager.start().onSuccess {
                    isRunning = true
                    updateNotificationWithLocation(getString(Res.string.gps_tracking_start), getString(Res.string.gps_tracking_first_pull_location_information))
                }.onError { e ->
                    updateNotification(getString(Res.string.error_gps_tracking), e.message ?: getString(Res.string.unknown_error))
                    stopSelf()
                }

                // Location updates'i dinle ve notification'ı güncelle
                launch {
                    gpsTrackingManager.observeLastLocation()
                        .collect { location ->
                            if (location != null) {
                                updateNotificationWithLocationData(location)
                            }
                        }
                }

                // Sync status'u dinle (her sync'te notification güncelle)
                launch {
                    gpsTrackingManager.observeSyncStatus()
                        .collect { syncStatus ->
                            // Sync durumu değiştiğinde mevcut location ile güncelle
                            val lastLocation = gpsTrackingManager.getLastLocation()
                            if (lastLocation != null) {
                                updateNotificationWithLocationData(lastLocation)
                            }
                        }
                }

                // State'i dinle ve notification'ı güncelle
                gpsTrackingManager.observeServiceState().collect { state ->
                    when (state) {
                        is ServiceState.Running -> {
                            // Location updates zaten hallediliyor
                        }
                        is ServiceState.Paused -> {
                            updateNotification(getString(Res.string.gps_tracking_pause), getString(Res.string.gps_tracking_pause_information))
                        }
                        is ServiceState.Error -> {
                            updateNotification(getString(Res.string.error_gps_tracking), state.domainException.message ?: "")
                        }
                        else -> {}
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                stopSelf()
            }
        }
    }
    private fun updateNotificationWithLocationData(location: GpsLocation) {
        serviceScope.launch {
            try {
                updateCompleteNotification(location)
            } catch (e: Exception) {
                // Fallback: basit güncelleme
                updateBasicNotification(location)
            }
        }
    }
    private suspend fun updateCompleteNotification(location: GpsLocation) {
        val stats = gpsTrackingManager.getStatistics()

        val config = gpsTrackingManager.getCurrentConfig()

        val title = buildString {
            append(getString(Res.string.gps_tracking))
            if (config.shouldRunBackgroundService()) append(" ${getString(Res.string.gps_tracking_scheduler)}")
        }

        val text = buildString {
            //  Koordinatlar + Accuracy + Hız

            append(" ${getString(Res.string.gps_tracking_location_desc, formatCoordinate(location.latitude, location.longitude))}")
            append(" | ${getString(Res.string.gps_tracking_location_accuracy_desc, location.accuracy.toInt())}")

            location.speed?.let {
                if(it > 0f){
                    val speedKmh = (it * 3.6).toInt()
                    append(" | $speedKmh km/s")
                }
            }
            append("\n")

            // Yön + Zaman
            location.bearing?.let {
                append("${getBearingDirection(it)} | ")
            }
            append("${formatTimeSince(location.timestamp)}\n")

            // İstatistikler
            if (stats != null) {
                append("\n ${getString(Res.string.gps_tracking_location_statics_desc,stats.totalLocations,stats.getFormattedDistance())}\n")

                // ☁️ Sync durumu
                if (stats.unsyncedCount > 0) {

                    append("${getString(Res.string.gps_tracking_location_sync_statics_desc,stats.unsyncedCount)}\n")
                } else {
                    append("${getString(Res.string.gps_tracking_location_syncall_desc)}\n")
                }
            } else {
                append("\n${getString(Res.string.gps_tracking_location_dataall_desc)}\n")
            }

            //Schedule bilgisi
            if (config.shouldRunBackgroundService()) {
                append("\n ${getString(Res.string.gps_tracking_location_scheduler_end_desc,config.endTimeHour,config.endTimeMinute)}")
            }
        }

        updateNotification(title, text, location)
    }
    private suspend fun updateBasicNotification(location: GpsLocation) {
        val title = getString(Res.string.gps_tracking_active)
        val text = buildString {
            append("📍 ${getString(Res.string.gps_tracking_location_statics_desc,String.format("%.4f", location.latitude), String.format("%.4f", location.longitude))}\n")
            append("🎯 ${getString(Res.string.gps_tracking_location_accuracy_desc, location.accuracy.toInt())}\n")
            append("⏰ ${formatTimeSince(location.timestamp)}")
        }

        updateNotification(title, text, location)
    }
    private fun getBearingDirection(bearing: Float): String {
        return when {
            bearing >= 337.5 || bearing < 22.5 -> "Kuzey ↑"
            bearing >= 22.5 && bearing < 67.5 -> "Kuzeydoğu ↗"
            bearing >= 67.5 && bearing < 112.5 -> "Doğu →"
            bearing >= 112.5 && bearing < 157.5 -> "Güneydoğu ↘"
            bearing >= 157.5 && bearing < 202.5 -> "Güney ↓"
            bearing >= 202.5 && bearing < 247.5 -> "Güneybatı ↙"
            bearing >= 247.5 && bearing < 292.5 -> "Batı ←"
            else -> "Kuzeybatı ↖"
        }
    }
    @SuppressLint("DefaultLocale")
    private fun formatCoordinate(lat: Double, lon: Double): String {
        return "${String.format("%.4f", lat)}, ${String.format("%.4f", lon)}"
    }
    private suspend fun formatTimeSince(timestamp: Long): String {
        val now = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val duration = now - instant
        return when {
            duration.inWholeSeconds < 60 -> getString(Res.string.time_just_now)
            duration.inWholeMinutes < 60 -> getString(Res.string.time_minutes_ago,duration.inWholeMinutes)
            duration.inWholeHours   < 24 -> getString(Res.string.time_hours_ago,duration.inWholeHours)
            duration.inWholeDays    < 7  -> getString(Res.string.time_days_ago,duration.inWholeDays)
            duration.inWholeDays    < 30 -> getString(Res.string.time_weeks_ago,duration.inWholeDays)
            duration.inWholeDays    < 365 -> getString(Res.string.time_months_ago,duration.inWholeDays)
            else -> getString(Res.string.time_years_ago,duration.inWholeDays / 365)
        }

    }
    private fun updateNotificationWithLocation(title: String, text: String) {
        updateNotification(title, text, null)
    }
    private fun stopTracking() {
        if (!isRunning) return

        serviceScope.launch {
            gpsTrackingManager.stop()
            isRunning = false
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun pauseTracking() {
        serviceScope.launch {
            gpsTrackingManager.pause()
            updateNotification(getString(Res.string.gps_tracking_pause), getString(Res.string.gps_tracking_pause_information))
        }
    }

    private fun resumeTracking() {
        serviceScope.launch {
            gpsTrackingManager.resume()
            updateNotification(getString(Res.string.gps_tracking_resume), getString(Res.string.gps_tracking_pull_location_information))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "GPS Tracking", NotificationManager.IMPORTANCE_LOW).apply {
                description = "GPS location tracking notifications"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(title: String, text: String, showLocation: Boolean = false, location: GpsLocation? = null): Notification {
        // Pending Intent for opening app
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Stop action
        val stopIntent = Intent(this, GpsTrackingService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // ⭐ YENİ: Pause action
        val pauseIntent = Intent(this, GpsTrackingService::class.java).apply {
            action = ACTION_PAUSE
        }
        val pausePendingIntent = PendingIntent.getService(
            this,
            1,
            pauseIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(text)
            .setBigContentTitle(title)

        if (showLocation && location != null) {
            bigTextStyle.setSummaryText("GPS Tracking")
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setStyle(bigTextStyle)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Duraklat",
                pausePendingIntent
            )
            .addAction(
                android.R.drawable.ic_delete,
                "Durdur",
                stopPendingIntent
            )
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(title: String, text: String, location: GpsLocation? = null) {
        val notification = createNotification(title, text, showLocation = location != null, location = location)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    //endregion

}