package com.repzone.data.service

import com.repzone.core.enums.GpsStatus
import com.repzone.core.enums.LocationAccuracy
import com.repzone.core.platform.Logger
import com.repzone.core.util.PermissionStatus
import com.repzone.core.util.extensions.now
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.rawExecute
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
import com.repzone.domain.common.businessRuleException
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.manager.gps.IPlatformGpsEnabler
import com.repzone.domain.platform.IPlatformServiceController
import com.repzone.domain.service.IPlatformGeocoder
import com.repzone.domain.util.notification.IPlatformNotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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
                          private val serviceController: IPlatformServiceController? = null,
                          private val iDAtabaseManager: IDatabaseManager,
                          private val iGeocoder: IPlatformGeocoder,
                          private val notificationHelper: IPlatformNotificationHelper,
                          private val gpsEnabler: IPlatformGpsEnabler,
                          private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
):
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
    private val _gpsStatus = MutableStateFlow<GpsStatus>(GpsStatus.DISABLED)
    private var platformLocationJob: Job? = null
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

            //Active kontrolü
            if (!config.isActive) {
                val message = "GPS tracking is not active (isActive = false)"
                _serviceState.value = ServiceState.Error(businessRuleException(ErrorCode.ERROR_GPS_ISNOT_ACTIVE))
                println("LocationService: $message")
                return Result.Error(businessRuleException(ErrorCode.ERROR_GPS_ISNOT_ACTIVE))
            }

            // İzinleri kontrol et
            val permissionStatus = platformProvider.checkPermissions()
            if (permissionStatus != PermissionStatus.Granted) {
                _serviceState.value = ServiceState.Error(DomainException.UnknownException(cause = Exception("Location permission not granted")))
                return Result.Error(DomainException.UnknownException(cause = Exception("Location permission not granted")))
            }

            // Platform location updates'i başlat
            startPlatformLocationUpdates(config)

            startGpsMonitoring()
            if (!platformProvider.isLocationEnabled()) {
                Logger.d("LocationService:️ GPS kapalı! Bildirim gösteriliyor...")
                notificationHelper.showGpsDisabledNotification()
            }

            isRunning = true
            isPaused = false
            _serviceState.value = ServiceState.Running
            // Schedule monitoring başlat (her zaman - service start/stop kontrolü için)
            startScheduleMonitoring(config)
            val deleteSql = "DELETE FROM GeoLocationEntity WHERE Time < (strftime('%s', 'now', 'utc', '-2 days') * 1000)"
            iDAtabaseManager.getSqlDriver().rawExecute(deleteSql)
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
            platformProvider.stopGpsStatusMonitoring()
            notificationHelper.dismissAllTrackingNotifications()

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
        val originalPauseState = isPaused
        return try {
            Logger.d("LOCATION_SERVICE", "forceGpsUpdate() - Schedule'dan bağımsız")

            isPaused = false

            platformProvider.stopLocationUpdates()
            delay(500)

            Logger.d("LOCATION_SERVICE", "Force Gps location isteniyor...")
            val locationResult = platformProvider.requestLocation()

            if (locationResult.isSuccess) {
                val location = locationResult.getOrThrow()

                // Validate
                if (!location.isValid()) {
                    Logger.d("LOCATION_SERVICE", "Invalid GPS location")
                    restartPeriodicUpdates()
                    isPaused = originalPauseState
                    return Result.Error(DomainException.UnknownException(cause = IllegalStateException("Invalid GPS location")))
                }

                Logger.d("LOCATION_SERVICE", "Valid location alındı: (${location.latitude}, ${location.longitude})")

                // Force sync ile işle (schedule kontrolü YOK!)
                processNewLocation(location, forceSync = true)

                delay(500)
                restartPeriodicUpdates()

                // Pause state'i geri döndür
                isPaused = originalPauseState

                Logger.d("LOCATION_SERVICE", "forceGpsUpdate başarılı!")
                Result.Success(location)

            } else {
                Logger.d("LOCATION_SERVICE", "Location alınamadı")
                restartPeriodicUpdates()
                isPaused = originalPauseState
                locationResult
            }

        } catch (e: Exception) {
            Logger.d("LOCATION_SERVICE", "forceGpsUpdate hatası: ${e.message}")
            restartPeriodicUpdates()
            isPaused = originalPauseState
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun getLastKnownLocation(): GpsLocation? {
        // Önce repository'den dene
        val repoLocation = locationRepository.getLastLocation()
        if (repoLocation != null) {
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

    override fun observeGpsStatus(): Flow<GpsStatus> {
        return _gpsStatus.asStateFlow()
    }
    //endregion


    //region Private Method

    private suspend fun startGpsMonitoring() {
        updateGpsStatus()

        platformProvider.startGpsStatusMonitoring { isEnabled ->
            coroutineScope.launch {
                if (isEnabled) {
                    // GPS açıldı
                    notificationHelper.dismissGpsDisabledNotification()
                } else {
                    // GPS kapatıldı
                    notificationHelper.showGpsDisabledNotification()

                    tryEnableGpsAutomatically()
                }

                updateGpsStatus()
            }
        }

        coroutineScope.launch {
            var lastGpsState = platformProvider.isLocationEnabled()
            while (isRunning && !isPaused) {
                delay(2 * 60 * 1000L) // 2 dakika

                val currentGpsState = platformProvider.isLocationEnabled()
                if (lastGpsState && !currentGpsState) {
                    notificationHelper.showGpsDisabledNotification()
                }
                if (!lastGpsState && currentGpsState) {
                    notificationHelper.dismissGpsDisabledNotification()
                    tryEnableGpsAutomatically()
                }
                updateGpsStatus()
                lastGpsState = currentGpsState
            }
        }
    }

    private fun tryEnableGpsAutomatically() {
        coroutineScope.launch {
            var success = false
            while (!success){
                try {
                    success = gpsEnabler.requestEnableGps()
                    if (success) {
                        Logger.d("LocationService","GPS otomatik açıldı!")
                        notificationHelper.dismissGpsDisabledNotification()
                        updateGpsStatus()
                    } else {
                        Logger.d("LocationService","Kullanıcı GPS açmayı reddetti")
                    }
                } catch (e: Exception) {
                    Logger.d("LocationService","GPS açma hatası: ${e.message}")
                    e.printStackTrace()
                }
            }

        }
    }

    private fun updateGpsStatus() {
        val isGpsEnabled = platformProvider.isLocationEnabled()

        _gpsStatus.value = when {
            isGpsEnabled -> GpsStatus.ENABLED
            else -> GpsStatus.NO_LOCATION
        }

        Logger.d("LocationService: GPS Status = ${_gpsStatus.value}")
    }

    private fun restartPeriodicUpdates() {
        currentConfig?.let { config ->
            if (isServiceRunning()) {
                startPlatformLocationUpdates(config)
            }
        }
    }
    private fun startScheduleMonitoring(config: GpsConfig) {
        coroutineScope.launch {
            var lastScheduleState = config.shouldRunBackgroundService()

            while (isRunning && !isPaused) {
                delay(60_000) // Her 1 dakikada kontrol et

                // Background service kontrolü
                val shouldRun = config.shouldRunBackgroundService()
                val currentlyRunning = serviceController?.isServiceRunning() ?: false

                when {
                    // Schedule içine girildi Service başlat
                    shouldRun && !currentlyRunning -> {
                        Logger.d("LOCATION_SERVICE", "Schedule içine girildi, Foreground Service başlatılıyor...")
                        serviceController?.startForegroundService()

                        // Location updatesi başlat
                        Logger.d("LOCATION_SERVICE", "▶Location updates başlatılıyor...")
                        startPlatformLocationUpdates(config)
                    }

                    // Schedule dışına çıkıldı Service durdur
                    !shouldRun && currentlyRunning -> {
                        Logger.d("LOCATION_SERVICE", "Schedule dışına çıkıldı, Foreground Service durduruluyor...")

                        //Location updates'i DURDUR (Job iptal + Platform durdur)
                        Logger.d("LOCATION_SERVICE", "Location updates durduruluyor...")
                        stopLocationUpdates()

                        // Service'i durdur
                        serviceController.stopForegroundService()

                        // Notification'ları temizle
                        try {
                            notificationHelper.dismissAllTrackingNotifications()
                        } catch (e: Exception) {
                            Logger.d("LOCATION_SERVICE", "Notification temizleme hatası: ${e.message}")
                        }
                    }
                }

                // Schedule durumu değişikliğini logla
                val currentScheduleState = config.shouldRunBackgroundService()
                if (lastScheduleState != currentScheduleState) {
                    if (currentScheduleState) {
                        Logger.d("LOCATION_SERVICE", "Schedule başladı!")
                    } else {
                        Logger.d("LOCATION_SERVICE", "Schedule bitti!")
                    }
                    lastScheduleState = currentScheduleState
                }
            }
        }
    }
    private fun startPlatformLocationUpdates(config: GpsConfig) {
        // Önce mevcut updates'i durdur
        stopLocationUpdates()

        Logger.d("LOCATION_SERVICE", "📡 Platform location updates başlatılıyor...")

        // Doğruluk seviyesini ayarla
        val accuracy = if (config.batteryOptimizationEnabled) {
            LocationAccuracy.BALANCED
        } else {
            LocationAccuracy.HIGH
        }
        platformProvider.setLocationAccuracy(accuracy)

        // Platform location updates'i başlat
        val intervalMs = config.gpsIntervalMinutes * 60 * 1000L

        val shouldProcess = isRunning &&
                !isPaused &&
                (currentConfig?.enableBackgroundTracking != true ||
                        config.shouldRunBackgroundService())
        if(shouldProcess){
            platformProvider.startLocationUpdates(
                intervalMs = intervalMs,
                minDistanceMeters = config.minDistanceMeters
            ) { location ->
                // Callback içinde job kontrolü yap
                if (platformLocationJob?.isActive == true) {
                    coroutineScope.launch {
                        // Schedule kontrolü ekle
                        if (isRunning && !isPaused) {
                            Logger.d("LOCATION_SERVICE", "Location alındı: (${location.latitude}, ${location.longitude})")
                            processNewLocation(location)
                        } else {
                            Logger.d("LOCATION_SERVICE", " Location atlandı (isRunning=$isRunning, isPaused=$isPaused)")
                        }
                    }
                } else {
                    Logger.d("LOCATION_SERVICE", "Location atlandı (Job iptal edilmiş)")
                }
            }
        }

        platformLocationJob = coroutineScope.launch {
            try {
                awaitCancellation()
            } catch (e: CancellationException) {
                Logger.d("LOCATION_SERVICE", "Platform location job iptal edildi")
                throw e
            }
        }

        Logger.d("LOCATION_SERVICE", "Platform location updates başlatıldı (interval=${intervalMs}ms)")
    }

    private fun stopLocationUpdates() {
        platformLocationJob?.cancel()
        platformLocationJob = null
        platformProvider.stopLocationUpdates()
    }

    private suspend fun processNewLocation(location: GpsLocation, forceSync: Boolean = false) {
        try {
            val now = now()
            val isDuplicate = location.id == lastProcessedLocationId
            val isTooRecent = (now - lastProcessedTime) < debouncePeriod

            // Force sync ise debounce/duplicate kontrollerini ATLA
            if (!forceSync && (isDuplicate || isTooRecent)) {
                Logger.d("LOCATION_SERVICE", "Location skipped - duplicate: $isDuplicate, too recent: $isTooRecent, id: ${location.id}")
                return
            }

            if(iGeocoder.isAvailable()){
                val adress = iGeocoder.getAddress(location.latitude,location.longitude)
                adress?.let {
                    location.reverseGeocoded = it.fullAddress
                }
            }

            val config = currentConfig ?: return

            if (!config.isActive) {
                Logger.d("LOCATION_SERVICE", "Location rejected - service not active")
                return
            }

            // Doğruluk kontrolü
            if (location.accuracy > config.accuracyThreshold) {
                Logger.d("LOCATION_SERVICE","Location rejected: accuracy too low (${location.accuracy}m > ${config.accuracyThreshold}m)")
                return
            }

            // Force sync ise mesafe kontrolünü ATLA
            if (!forceSync && config.batteryOptimizationEnabled) {
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

                // Firebase gönderimi: forceSync VEYA (schedule içi VE shouldSendToFirebase)
                val shouldSendFirebase = forceSync ||
                        (config.shouldSendToFirebase() && config.shouldRunBackgroundService())

                if (shouldSendFirebase) {
                    Logger.d("LOCATION_SERVICE", "Firebase'e gönderiliyor (forceSync=$forceSync)...")
                    coroutineScope.launch {
                        //sendToFirebase(location)
                    }
                } else {
                    Logger.d("LOCATION_SERVICE", "Firebase gönderimi atlandı (schedule dışı)")
                }

                Logger.d("LOCATION_SERVICE","Location saved: ${location.toReadableString()}")
            }.onError { e ->
                Logger.error("LOCATION_SERVICE",e)
            }

        } catch (e: Exception) {
            Logger.error("LOCATION_SERVICE",e)
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

            if(iGeocoder.isAvailable()){
                val adress = iGeocoder.getAddress(location.latitude,location.longitude)
                adress?.let {
                    location.reverseGeocoded = it.fullAddress
                }
            }

            val config = currentConfig ?: return

            if (!config.isActive) {
                Logger.d("LOCATION_SERVICE: Location rejected - service not active")
                return
            }

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
                if (config.shouldSendToFirebase() && config.shouldRunBackgroundService()) {
                    coroutineScope.launch {
                        //sendToFirebase(location)
                    }
                }
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