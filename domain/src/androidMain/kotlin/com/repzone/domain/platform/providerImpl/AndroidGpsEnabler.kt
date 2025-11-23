package com.repzone.domain.platform.providerImpl

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.repzone.domain.manager.gps.IPlatformGpsEnabler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidGpsEnabler(private val context: Context): IPlatformGpsEnabler {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    companion object {
        const val REQUEST_CHECK_SETTINGS = 9001
        private var enableGpsCallback: ((Boolean) -> Unit)? = null

        private var currentActivity: Activity? = null

        /**
         * Activity lifecycle tracking için Application.ActivityLifecycleCallbacks
         */
        fun registerActivityTracker(application: Application) {
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    currentActivity = activity
                }

                override fun onActivityPaused(activity: Activity) {
                    if (currentActivity == activity) {
                        currentActivity = null
                    }
                }

                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }

        fun handleActivityResult(requestCode: Int, resultCode: Int) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                val success = resultCode == Activity.RESULT_OK
                enableGpsCallback?.invoke(success)
                enableGpsCallback = null
            }
        }
    }

    override suspend fun requestEnableGps(): Boolean {
        val activity = currentActivity
        if (activity == null) {
            return false
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    10000L
                ).build()

                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true)  // Dialog her zaman göster

                val settingsClient = LocationServices.getSettingsClient(context)
                val task = settingsClient.checkLocationSettings(builder.build())

                task.addOnSuccessListener {
                    // GPS zaten açık
                    if (continuation.isActive) {
                        continuation.resume(true)
                    }
                }

                task.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        try {
                            enableGpsCallback = { success ->
                                if (continuation.isActive) {
                                    continuation.resume(success)
                                }
                            }

                            // Dialog göster
                            exception.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS
                            )

                        } catch (sendEx: IntentSender.SendIntentException) {
                            enableGpsCallback = null
                            if (continuation.isActive) {
                                continuation.resume(false)
                            }
                        }
                    } else {
                        println("AndroidGpsEnabler: Cannot enable GPS - ${exception.message}")
                        if (continuation.isActive) {
                            continuation.resume(false)
                        }
                    }
                }

                continuation.invokeOnCancellation {
                    enableGpsCallback = null
                    println("AndroidGpsEnabler: Request cancelled")
                }

            } catch (e: Exception) {
                println("AndroidGpsEnabler: Error - ${e.message}")
                enableGpsCallback = null
                if (continuation.isActive) {
                    continuation.resume(false)
                }
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}