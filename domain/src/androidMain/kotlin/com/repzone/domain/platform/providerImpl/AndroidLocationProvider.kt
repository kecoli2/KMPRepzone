package com.repzone.domain.platform.providerImpl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.platform.Logger
import com.repzone.core.platform.randomUUID
import com.repzone.core.util.PermissionStatus
import com.repzone.core.util.extensions.toMilisecond
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.platform.IPlatformLocationProvider
import com.repzone.domain.common.Result
import com.repzone.domain.common.businessRuleException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class AndroidLocationProvider(private val context: Context): IPlatformLocationProvider {
    //region Field
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback? = null
    private var currentAccuracy: LocationAccuracy = LocationAccuracy.HIGH
    private var currentConfig: GpsConfig? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun setConfig(config: GpsConfig) {
        this.currentConfig = config

        currentAccuracy = when {
            config.batteryOptimizationEnabled && config.accuracyThreshold > 100f -> LocationAccuracy.BALANCED
            config.gpsIntervalMinutes <= 1 && config.accuracyThreshold < 250f  -> LocationAccuracy.HIGH
            config.gpsIntervalMinutes <= 5 -> LocationAccuracy.BALANCED
            else -> LocationAccuracy.LOW
        }
        Logger.d("AndroidLocationProvider: Config updated - accuracy: $currentAccuracy, battery opt: ${config.batteryOptimizationEnabled}")
    }

    @SuppressLint("MissingPermission")
    override suspend fun requestLocation(timeoutSecond: Int): Result<GpsLocation> {
        return try {
            // Mevcut periyodik callback'i geçici olarak durdur
            val previousCallback = locationCallback
            if (previousCallback != null) {
                fusedLocationClient.removeLocationUpdates(previousCallback)
                Logger.d("AndroidLocationProvider: Paused periodic updates for force GPS request")
            }

            // Force GPS request
            val result = withTimeoutOrNull(timeoutSecond.toMilisecond()) {
                suspendCancellableCoroutine<Result<GpsLocation>> { continuation ->
                    try {
                        if (!hasLocationPermission()) {
                            continuation.resume(
                                Result.Error(
                                    businessRuleException(
                                        ErrorCode.GPS_PERMISSION_NOT_GRANTED,
                                        cause = SecurityException("Location permission not granted")
                                    )
                                )
                            )
                            return@suspendCancellableCoroutine
                        }

                        var isResumed = false

                        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
                            .setMinUpdateIntervalMillis(0)
                            .setMaxUpdates(1)
                            .setWaitForAccurateLocation(true)
                            .build()

                        lateinit var callback: LocationCallback

                        callback = object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                if (!isResumed) {
                                    isResumed = true
                                    fusedLocationClient.removeLocationUpdates(callback)

                                    val location = result.lastLocation
                                    if (location != null) {
                                        Logger.d("AndroidLocationProvider: Force GPS received - accuracy: ${location.accuracy}m")
                                        continuation.resume(Result.Success(location.toGpsLocation()))
                                    } else {
                                        continuation.resume(
                                            Result.Error(
                                                businessRuleException(
                                                    ErrorCode.UNKNOWN_ERROR,
                                                    cause = IllegalStateException("Location is null")
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            callback,
                            Looper.getMainLooper()
                        )

                        continuation.invokeOnCancellation {
                            fusedLocationClient.removeLocationUpdates(callback)
                        }

                    } catch (e: Exception) {
                        if (!continuation.isCompleted) {
                            continuation.resume(
                                Result.Error(
                                    businessRuleException(
                                        ErrorCode.UNKNOWN_ERROR,
                                        cause = e
                                    )
                                )
                            )
                        }
                    }
                }
            } ?: Result.Error(businessRuleException(ErrorCode.ERROR_GPS_TIMEOUT))

            // Periyodik callback'i tekrar başlat (eğer daha önce çalışıyorsa)
            if (previousCallback != null) {
                restorePeriodicUpdates(previousCallback)
            }

            result
        } catch (e: Exception) {
            Result.Error(businessRuleException(ErrorCode.UNKNOWN_ERROR, cause = e))
        }
    }

    @SuppressLint("MissingPermission")
    private fun restorePeriodicUpdates(callback: LocationCallback) {
        val config = currentConfig ?: return

        val intervalMs = config.gpsIntervalMinutes * 60 * 1000L
        val minDistance = config.minDistanceMeters

        val priority = when {
            config.batteryOptimizationEnabled && currentAccuracy == LocationAccuracy.BALANCED ->
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            currentAccuracy == LocationAccuracy.HIGH ->
                Priority.PRIORITY_HIGH_ACCURACY
            currentAccuracy == LocationAccuracy.BALANCED ->
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            currentAccuracy == LocationAccuracy.LOW ->
                Priority.PRIORITY_LOW_POWER
            else ->
                Priority.PRIORITY_PASSIVE
        }

        val locationRequest = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateDistanceMeters(minDistance)
            .setMinUpdateIntervalMillis(5000)
            .setWaitForAccurateLocation(true)
            .setMaxUpdateDelayMillis(intervalMs)
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        Logger.d("AndroidLocationProvider: Resumed periodic updates after force GPS request")
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates(
        intervalMs: Long,
        minDistanceMeters: Float,
        callback: (GpsLocation) -> Unit
    ) {
        if (!hasLocationPermission()) {
            throw SecurityException("Location permission not granted")
        }

        val config = currentConfig ?: GpsConfig()

        val priority = when {
            config.batteryOptimizationEnabled && currentAccuracy == LocationAccuracy.BALANCED -> Priority.PRIORITY_BALANCED_POWER_ACCURACY
            currentAccuracy == LocationAccuracy.HIGH -> Priority.PRIORITY_HIGH_ACCURACY
            currentAccuracy == LocationAccuracy.BALANCED -> Priority.PRIORITY_BALANCED_POWER_ACCURACY
            currentAccuracy == LocationAccuracy.LOW -> Priority.PRIORITY_LOW_POWER
            else -> Priority.PRIORITY_PASSIVE
        }

        Logger.d("AndroidLocationProvider: Starting location updates - priority: $priority, interval: ${intervalMs}ms, minDistance: ${minDistanceMeters}m")

        val locationRequest = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateDistanceMeters(minDistanceMeters)
            .setMinUpdateIntervalMillis(5000)  // Min 5 saniye GPS sensörü bekle
            .setWaitForAccurateLocation(true) //HASSAS GPS BEKLIYOR BELKI BURADA BIR OPTIMIZASYON YAPARIZ HALUK BEYLE KONUSALIÖ
            .setMaxUpdateDelayMillis(intervalMs)
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)  // En hassas
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                     if (location.accuracy <= config.accuracyThreshold) {
                        callback(location.toGpsLocation())
                    } else {
                        Logger.d("AndroidLocationProvider: Location rejected - accuracy ${location.accuracy}m > threshold ${config.accuracyThreshold}m")
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    override fun stopLocationUpdates() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
            locationCallback = null
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): GpsLocation? {
        return suspendCancellableCoroutine { continuation ->
            try {
                if (!hasLocationPermission()) {
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    continuation.resume(location?.toGpsLocation())
                }.addOnFailureListener {
                    continuation.resume(null)
                }

            } catch (e: Exception) {
                continuation.resume(null)
            }
        }
    }

    override fun checkPermissions(): PermissionStatus {
        val fineLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return when {
            fineLocationGranted || coarseLocationGranted -> PermissionStatus.Granted
            else -> PermissionStatus.Denied(true)
        }
    }

    override suspend fun requestPermissions(): PermissionStatus {
        // Bu fonksiyon Activity context gerektirir
        // Activity'den çağrılmalı
        return checkPermissions()
    }

    override fun hasBackgroundPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 10'dan önce background permission yok
        }
    }

    override suspend fun requestBackgroundPermission(): PermissionStatus {
        // Bu fonksiyon Activity context gerektirir
        return if (hasBackgroundPermission()) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(true)
        }
    }

    override fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    }

    override fun setLocationAccuracy(priority: LocationAccuracy) {
        currentAccuracy = priority
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun hasLocationPermission(): Boolean {
        return checkPermissions() == PermissionStatus.Granted
    }
    private fun Location.toGpsLocation(): GpsLocation {
        return GpsLocation(
            id = randomUUID(),
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            timestamp = time,
            speed = if (hasSpeed()) speed else null,
            bearing = if (hasBearing()) bearing else null,
            altitude = if (hasAltitude()) altitude else null,
            provider = provider ?: "unknown",
            isSynced = false
        )
    }
    //endregion
}