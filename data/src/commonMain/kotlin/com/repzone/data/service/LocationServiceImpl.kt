package com.repzone.data.service

import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.platform.Logger
import com.repzone.core.util.PermissionStatus
import com.repzone.core.util.extensions.now
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import com.repzone.domain.platform.IPlatformLocationProvider
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.service.ILocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import com.repzone.domain.common.Result
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.util.isRecent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *  Sorumluluk: GPS toplama servisini yönetir
 *  Özellikler:
 *
 * PlatformLocationProvider kullanımı
 * Accuracy ve distance filtering
 * Battery optimization logic
 * Service state management
 *
 *
 * İşleyiş:
 *
 * startService() çağrılır
 * İzinler kontrol edilir (Ama bunu zaten splash da yapıyoruz kullanıcı izni tekrardan kapatabilir)
 * Platform location updates başlatılır
 * Her GPS güncellemesinde processNewLocation() çalışır
 *
 * Accuracy kontrolü
 * Distance kontrolü (battery optimization)
 * Repository'ye kayıt
 */

class LocationServiceImpl(private val platformProvider: IPlatformLocationProvider,
                          private val locationRepository: ILocationRepository,
                          private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)):
    ILocationService {
    //region Field
    private val _serviceState = MutableStateFlow<ServiceState>(ServiceState.Idle)
    private val _locationUpdates = MutableStateFlow<GpsLocation?>(null)

    private var currentConfig: GpsConfig? = null
    private var isRunning = false
    private var isPaused = false
    private var lastProcessedTime: Long = 0
    private val debouncePeriod = 3000L // 3 saniye
    private var lastProcessedLocationId: String? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun startService(config: GpsConfig): Result<Unit> {
        return try {
            if (isRunning) {
                return Result.Success(Unit)
            }

            _serviceState.value = ServiceState.Starting

            // Konfigürasyonu kaydet
            currentConfig = config

            //Config'i platform provider'a gönder
            platformProvider.setConfig(config)

            //Schedule kontrolü
            if (config.enableSchedule && !config.isWithinSchedule()) {
                val minutesUntilNext = config.getMinutesUntilNextScheduledTime()
                val message = "Şu an çalışma saatleri dışında. Bir sonraki çalışma: $minutesUntilNext dakika sonra"
                _serviceState.value = ServiceState.Error(DomainException.BusinessRuleException(ErrorCode.UNKNOWN_ERROR, cause = Exception(message)))
                println(message)

                // Bir sonraki çalışma zamanında otomatik başlat
                scheduleNextStart(config, minutesUntilNext)

                return Result.Error(DomainException.UnknownException(cause = IllegalStateException(message)))
            }

            // İzinleri kontrol et
            val permissionStatus = platformProvider.checkPermissions()
            if (permissionStatus != PermissionStatus.Granted) {
                _serviceState.value = ServiceState.Error(DomainException.UnknownException(cause = Exception("Location permission not granted")))
                return Result.Error(DomainException.UnknownException(cause = Exception("Location permission not granted")))
            }

            // Platform location updates'i başlat
            startPlatformLocationUpdates(config)

            // ⭐ Schedule aktifse, periyodik kontrol başlat
            if (config.enableSchedule) {
                startScheduleMonitoring(config)
            }

            isRunning = true
            isPaused = false
            _serviceState.value = ServiceState.Running

            Result.Success(Unit)
        } catch (e: Exception) {
            _serviceState.value = ServiceState.Error(DomainException.UnknownException(cause = Exception(e.message ?: "Unknown error")))
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun stopService(): Result<Unit> {
        return try {
            if (!isRunning) {
                return Result.Success(Unit)
            }

            _serviceState.value = ServiceState.Stopping

            // Platform updates'i durdur
            platformProvider.stopLocationUpdates()

            isRunning = false
            isPaused = false
            currentConfig = null

            _serviceState.value = ServiceState.Idle

            Result.Success(Unit)
        } catch (e: Exception) {
            _serviceState.value = ServiceState.Error(DomainException.UnknownException(cause = e))
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun pauseService(): Result<Unit> {
        return try {
            if (!isRunning || isPaused) {
                return Result.Error(DomainException.UnknownException(cause = IllegalStateException("Service not running or already paused")))
            }

            platformProvider.stopLocationUpdates()

            isPaused = true
            _serviceState.value = ServiceState.Paused

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun resumeService(): Result<Unit> {
        return try {
            if (!isRunning || !isPaused) {
                return Result.Error(DomainException.UnknownException(cause = IllegalStateException("Service not paused")))
            }

            val config = currentConfig ?: return Result.Error(DomainException.UnknownException(cause = IllegalStateException("No config available")))

            startPlatformLocationUpdates(config)

            isPaused = false
            _serviceState.value = ServiceState.Running

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override fun isServiceRunning(): Boolean {
        return isRunning && !isPaused
    }

    override suspend fun getCurrentLocation(): Result<GpsLocation> {
        val result = platformProvider.requestLocation()
        if (result.isSuccess) {
            val location = result.getOrNull()
            if (location != null) {
                processNewLocation(location)
            }
        }
        return result
    }

    override suspend fun forceGpsUpdate(): Result<GpsLocation> {
        return try {
            platformProvider.stopLocationUpdates()
            delay(500)
            val locationResult = platformProvider.requestLocation()
            if (locationResult.isSuccess) {
                val location = locationResult.getOrThrow()

                // Validate
                if (!location.isValid()) {
                    restartPeriodicUpdates()
                    return Result.Error(DomainException.UnknownException(cause = IllegalStateException("Invalid GPS location")))
                }
                processNewLocation(location)
                delay(1000)
                restartPeriodicUpdates()
                Result.Success(location)
            } else {
                restartPeriodicUpdates()
                Result.Error(DomainException.UnknownException(cause = IllegalStateException("Force GPS failed")))
            }
        } catch (e: Exception) {
            restartPeriodicUpdates()
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun getLastKnownLocation(): GpsLocation? {
        // Önce repository'den dene
        val repoLocation = locationRepository.getLastLocation()
        if (repoLocation != null && repoLocation.isRecent(maxAgeMillis = 60_000)) {
            return repoLocation
        }

        // Platform'dan son bilinen konumu al
        return platformProvider.getLastKnownLocation()
    }

    override fun observeLocationUpdates(): Flow<GpsLocation> {
        return kotlinx.coroutines.flow.flow {
            _locationUpdates.collect { location ->
                location?.let { emit(it) }
            }
        }
    }

    override fun observeServiceState(): Flow<ServiceState> {
        return _serviceState.asStateFlow()
    }
    //endregion


    //region Private Method

    private fun restartPeriodicUpdates() {
        currentConfig?.let { config ->
            if (isServiceRunning()) {
                startPlatformLocationUpdates(config)
            }
        }
    }
    private fun scheduleNextStart(config: GpsConfig, delayMinutes: Int) {
        coroutineScope.launch {
            println("Schedule: $delayMinutes dakika sonra servis başlatılacak...")
            delay(delayMinutes * 60 * 1000L)
            startService(config)
        }
    }

    private fun startScheduleMonitoring(config: GpsConfig) {
        coroutineScope.launch {
            while (isRunning && !isPaused) {
                delay(60_000) // Her 1 dakikada kontrol et

                if (!config.isWithinSchedule()) {
                    // Çalışma saatleri dışına çıkıldı
                    Logger.d("Schedule: Çalışma saatleri dışına çıkıldı, servis duraklatılıyor...")
                    pauseService()

                    // Bir sonraki çalışma zamanında devam et
                    val minutesUntilNext = config.getMinutesUntilNextScheduledTime()
                    scheduleNextResume(config, minutesUntilNext)
                    break
                }
            }
        }
    }

    private fun scheduleNextResume(config: GpsConfig, delayMinutes: Int) {
        coroutineScope.launch {
            println("Schedule: $delayMinutes dakika sonra servis devam edecek...")
            delay(delayMinutes * 60 * 1000L)
            resumeService().onSuccess {
                // Resume sonrası monitoring'i tekrar başlat
                if (config.enableSchedule) {
                    startScheduleMonitoring(config)
                }
            }
        }
    }

    private fun startPlatformLocationUpdates(config: GpsConfig) {
        // Doğruluk seviyesini ayarla
        val accuracy = if (config.batteryOptimizationEnabled) {
            LocationAccuracy.BALANCED
        } else {
            LocationAccuracy.HIGH
        }
        platformProvider.setLocationAccuracy(accuracy)

        // Platform location updates'i başlat
        val intervalMs = config.gpsIntervalMinutes * 60 * 1000L

        platformProvider.startLocationUpdates(
            intervalMs = intervalMs,
            minDistanceMeters = config.minDistanceMeters
        ) { location ->
            coroutineScope.launch {
                processNewLocation(location)
            }
        }
    }
    private suspend fun processNewLocation(location: GpsLocation) {
        try {
            val now = now()
            val isDuplicate = location.id == lastProcessedLocationId
            val isTooRecent = (now - lastProcessedTime) < debouncePeriod

            if (isDuplicate || isTooRecent) {
                Logger.d("LOCATION_SERVICE", "Location skipped - duplicate: $isDuplicate, too recent: $isTooRecent, id: ${location.id}")
                return
            }

            val config = currentConfig ?: return

            // Doğruluk kontrolü
            if (location.accuracy > config.accuracyThreshold) {
                Logger.d("LOCATION_SERVICE","Location rejected: accuracy too low (${location.accuracy}m > ${config.accuracyThreshold}m)")
                return
            }

            // Mesafe kontrolü (battery optimization için)
            if (config.batteryOptimizationEnabled) {
                val lastLocation = locationRepository.getLastLocation()
                if (lastLocation != null) {
                    val distance = locationRepository.calculateDistance(lastLocation, location)
                    if (distance < config.minDistanceMeters) {
                        Logger.d("LOCATION_SERVICE","Location rejected: distance too small (${distance}m < ${config.minDistanceMeters}m)")
                        return
                    }
                }
            }

            // Konumu kaydet
            locationRepository.saveLocation(location).onSuccess {
                _locationUpdates.value = location
                lastProcessedLocationId = location.id
                lastProcessedTime = now
                Logger.d("LOCATION_SERVICE","Location saved: ${location.toReadableString()}")
            }.onError { e ->
                Logger.error("LOCATION_SERVICE",e)
            }

        } catch (e: Exception) {
            Logger.error("LOCATION_SERVICE",e)
        }
    }

    //endregion
}