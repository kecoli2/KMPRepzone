package com.repzone.mobile.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.repzone.core.platform.Logger

class GpsEnableActivity: Activity() {
    //region Field
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }
    //endregion

    //region Properties
    //endregion

    //region Constructor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationSettings()
    }
    //endregion

    //region Public Method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        Logger.d("GpsEnableActivity: uyser enabled GPS")
                    }
                    RESULT_CANCELED -> {
                        Logger.d("GpsEnableActivity:User declined GPS")
                    }
                }
                finish()
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun checkLocationSettings() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Logger.d("GpsEnableActivity: GPS is already enabled")
            finish()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Logger.d("GpsEnableActivity: Error showing dialog - ${sendEx.message}")
                    finish()
                }
            } else {
                Logger.d("GpsEnableActivity: Cannot resolve - ${exception.message}")
                finish()
            }
        }
    }
    //endregion
}