package com.repzone.mobile.reciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.repzone.core.platform.Logger
import com.repzone.domain.util.notification.IPlatformNotificationHelper
import com.repzone.mobile.ui.GpsEnableActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.jvm.java

class GpsStatusReceiver: BroadcastReceiver(), KoinComponent {
    //region Field
    private val notificationHelper: IPlatformNotificationHelper by inject()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            context?.let { ctx ->
                val pendingResult = goAsync()
                val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                Logger.d("GpsStatusReceiver: GPS=$isGpsEnabled, Network=$isNetworkEnabled")

                CoroutineScope(Dispatchers.IO).launch {
                    when {
                        // GPS ve Network ikisi de kapalı → KRITIK
                        !isGpsEnabled && !isNetworkEnabled -> {
                            notificationHelper.showGpsDisabledNotification()

                            // Otomatik aç dialog'unu göster
                            showEnableLocationDialog(ctx)
                        }

                        // Sadece GPS kapalı
                        !isGpsEnabled -> {
                            notificationHelper.showGpsDisabledNotification()

                            // Otomatik aç dialog'unu göster
                            showEnableLocationDialog(ctx)
                        }

                        // GPS açık
                        isGpsEnabled -> {
                            notificationHelper.dismissGpsDisabledNotification()
                        }
                    }
                }
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun showEnableLocationDialog(context: Context) {
        // Intent ile GPS açma dialog'unu başlat
        val intent = Intent(context, GpsEnableActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
    }
    //endregion
}