package com.repzone.mobile.managers.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.system.Os.close
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.getValue


class LocationServiceAndroid(private val app: Application): ILocationService {
    //region Field
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(app) }
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun ensurePermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(app, Manifest.permission.ACCESS_COARSE_LOCATION)
        return (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(highAccuracy: Boolean): GeoPoint? {
        if (!ensurePermission()) return null

        // 1) En hızlı yol: lastKnown
        val last = fused.lastLocation.await()
        if (last != null) {
            return GeoPoint(last.latitude, last.longitude, last.accuracy?.toDouble())
        }

        // 2) Aktif tek atımlık istek (Android 12+ currentLocation API’si):
        val priority = if (highAccuracy) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val loc = fused.getCurrentLocation(priority, null).await()
        return loc?.let { GeoPoint(it.latitude, it.longitude, it.accuracy?.toDouble()) }
    }

    @SuppressLint("MissingPermission")
    override fun observeLocationUpdates(highAccuracy: Boolean, minTimeMillis: Long, minDistanceMeters: Float): Flow<GeoPoint> {
        return callbackFlow {
            if (!ensurePermission()) {
                close(IllegalStateException("Location permission not granted")); return@callbackFlow
            }
            val priority = if (highAccuracy) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
            val request = LocationRequest.Builder(priority, minTimeMillis)
                .setMinUpdateDistanceMeters(minDistanceMeters)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        trySend(GeoPoint(it.latitude, it.longitude, it.accuracy?.toDouble()))
                    }
                }
            }
            fused.requestLocationUpdates(request, callback, app.mainLooper)
            awaitClose { fused.removeLocationUpdates(callback) }
        }

    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}