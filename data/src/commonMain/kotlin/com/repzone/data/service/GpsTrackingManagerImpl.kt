package com.repzone.data.service

import com.repzone.core.util.PermissionStatus
import com.repzone.core.util.extensions.now
import com.repzone.domain.common.DomainException
import com.repzone.domain.manager.gps.IGpsTrackingManager
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.platform.IPlatformLocationProvider
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.service.IGpsConfigManager
import com.repzone.domain.service.ILocationService
import com.repzone.domain.common.Result
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.model.SyncResult
import com.repzone.domain.model.SyncStatus
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.LocationMetadata
import com.repzone.domain.model.gps.ServiceState
import com.repzone.domain.model.gps.TrackingStatistics
import com.repzone.domain.service.IGpsDataSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 *
 * Sorumluluk: ANA KOORDİNATÖR - Tüm servisleri yönetir
 * Dependencies:
 *
 * LocationService
 * LocationRepository
 * GpsDataSyncService
 * GpsConfigManager
 * PlatformLocationProvider
 *
 *
 *  İşleyiş:
 *
 * initialize() - Config validate ve kaydet
 * start() - LocationService + DataSync başlat
 * observeXXX() - Tüm state'leri observable yap
 * updateXXX() - Config değişikliklerinde servisleri restart et
 */

class GpsTrackingManagerImpl(private val locationService: ILocationService,
                             private val iLocationRepository: ILocationRepository,
                             private val configManager: IGpsConfigManager,
                             private val platformProvider: IPlatformLocationProvider,
                             private val dataSyncService: IGpsDataSyncService
): IGpsTrackingManager {
    //region Field
    private var isInitialized = false
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun initialize(config: GpsConfig): Result<Unit> {
        return try {
            // Konfigürasyonu validate et
            val validationResult = config.validate()
            if (validationResult.isError) {
                return validationResult
            }

            // Konfigürasyonu kaydet
            val updateResult = configManager.updateConfig(config)
            if (updateResult.isError) {
                return updateResult
            }

            isInitialized = true
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun start(): Result<Unit> {
        if (!isInitialized) {
            return Result.Error(DomainException.UnknownException(cause = IllegalArgumentException("Manager not initialized. Call initialize() first.")))
        }

        // İzinleri kontrol et
        val permissionStatus = platformProvider.checkPermissions()
        if (permissionStatus != PermissionStatus.Granted) {
            return Result.Error(DomainException.UnknownException(cause = Exception("Location permission not granted")))
        }

        val config = configManager.getConfig()

        // Location service'i başlat
        val serviceResult = locationService.startService(config)
        if (serviceResult.isError) {
            return serviceResult
        }

        // Data sync'i planla
        dataSyncService.schedulePeriodicSync(config.serverSyncIntervalMinutes)

        // ⭐ YENİ: Auto sync özelliği aktifse, location updates'i dinle
        if (config.autoSyncOnGpsUpdate) {
            startAutoSyncObserver()
        }

        return Result.Success(Unit)
    }

    override suspend fun stop(): Result<Unit> {
        return try {
            locationService.stopService().getOrThrow()
            //dataSyncService.cancelScheduledSync()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause =e ))
        }
    }

    override suspend fun pause(): Result<Unit> {
        return locationService.pauseService()
    }

    override suspend fun resume(): Result<Unit> {
        return locationService.resumeService()
    }

    override suspend fun forceGpsUpdate(): Result<GpsLocation> {
        val result = locationService.forceGpsUpdate()
        if (result.isSuccess) {
            val location = result.getOrNull()
            if (location != null) {
                // Yeni konumu kaydet
                iLocationRepository.saveLocation(location)
            }
        }
        return result
    }

    override suspend fun forceSyncNow(): Result<SyncResult> {
        return dataSyncService.syncToServer()
    }

    override suspend fun getLocationMetadata(): LocationMetadata {
        val lastLocation = iLocationRepository.getLastLocation()
        val timeSinceLastGps = iLocationRepository.getTimeSinceLastGps()

        // Eğer bir önceki konum varsa mesafe hesapla
        val distanceFromLast = lastLocation?.let { last ->
            val history = iLocationRepository.getLocationHistory(2)
            if (history.size >= 2) {
                iLocationRepository.calculateDistance(history[0], history[1])
            } else null
        }

        return LocationMetadata(
            lastGpsTime = lastLocation?.timestamp,
            timeSinceLastGps = timeSinceLastGps,
            lastLatitude = lastLocation?.latitude,
            lastLongitude = lastLocation?.longitude,
            distanceFromLast = distanceFromLast,
            accuracy = lastLocation?.accuracy,
            provider = lastLocation?.provider
        )
    }

    override suspend fun getLastLocation(): GpsLocation? {
        return iLocationRepository.getLastLocation()
    }

    override fun getCurrentConfig(): GpsConfig {
        return configManager.getConfig()
    }

    override suspend fun updateGpsInterval(minutes: Int): Result<Unit> {
        val result = configManager.updateGpsInterval(minutes)
        if (result.isError) {
            return result
        }

        // Servis çalışıyorsa restart et
        if (locationService.isServiceRunning()) {
            val config = configManager.getConfig()
            locationService.stopService()
            locationService.startService(config)
        }
        return Result.Success(Unit)
    }

    override suspend fun updateSyncInterval(minutes: Int): Result<Unit> {
        val result = configManager.updateSyncInterval(minutes)
        if (result.isError) {
            return result
        }

        // Sync schedule'ı güncelle
        dataSyncService.cancelScheduledSync()
        dataSyncService.schedulePeriodicSync(minutes)

        return Result.Success(Unit)
    }

    override fun observeServiceState(): Flow<ServiceState> {
        return locationService.observeServiceState()
    }

    override fun observeLastLocation(): Flow<GpsLocation?> {
        return iLocationRepository.observeLastLocation()
    }

    override fun observeSyncStatus(): Flow<SyncStatus> {
        return dataSyncService.observeSyncStatus()
    }

    override suspend fun checkPermissions(): PermissionStatus {
        return platformProvider.checkPermissions()
    }

    override suspend fun requestPermissions(): PermissionStatus {
        return platformProvider.requestPermissions()
    }

    private fun calculateTotalDistance(locations: List<GpsLocation>): Double {
        if (locations.size < 2) return 0.0

        var totalDistance = 0.0
        for (i in 0 until locations.size - 1) {
            totalDistance += iLocationRepository.calculateDistance(locations[i], locations[i + 1])
        }
        return totalDistance
    }

    suspend fun getStatistics(): TrackingStatistics {
        val history = iLocationRepository.getLocationHistory(100)
        val totalDistance = calculateTotalDistance(history)
        val avgAccuracy = history.map { it.accuracy }.average().toFloat()
        val timeSinceLastGps = iLocationRepository.getTimeSinceLastGps()

        return TrackingStatistics(
            totalLocations = history.size,
            totalDistanceMeters = totalDistance,
            averageAccuracy = avgAccuracy,
            timeSinceLastGps = timeSinceLastGps,
            unsyncedCount = iLocationRepository.getUnsyncedLocations().size
        )
    }

    suspend fun cleanupOldData(daysToKeep: Int = 7): Result<Int> {
        val cutoffTime = now() - (daysToKeep * 24 * 60 * 60 * 1000L)
        return iLocationRepository.clearOldLocations(cutoffTime)
    }
    //endregion

    //region Private Method
    private fun startAutoSyncObserver() {
        CoroutineScope(SupervisorJob() + Dispatchers.Default
        ).launch {
            iLocationRepository.observeLocationUpdates()
                .collect { newLocation ->
                    // Yeni GPS alındığında hemen sync yap
                    println("Auto sync: New GPS received, triggering sync...")
                    dataSyncService.syncToServer()
                        .onSuccess { result ->
                            println("Auto sync completed: ${result.successCount} locations synced")
                        }
                        .onError { error ->
                            println("Auto sync failed: ${error.message}")
                        }
                }
        }
    }
    //endregion
}