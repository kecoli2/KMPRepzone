package com.repzone.data.repository.imp

import com.repzone.core.enums.SyncStatusType
import com.repzone.core.util.extensions.now
import com.repzone.data.mapper.GeoLocationEntityDbMapper
import com.repzone.database.GeoLocationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.insert
import com.repzone.database.runtime.rawExecute
import com.repzone.database.runtime.select
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.util.DistanceCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import com.repzone.domain.common.Result
import com.repzone.domain.model.SyncStatus

/**
 * Location Repository Implementation
 * GPS verilerinin saklanması ve yönetimi
 *
 * TODO: Database implementasyonu eklenecek
 * Şimdilik in-memory storage kullanıyoruz
 *
 * Özellikler
 * ----------------------------------------
 * In-memory storage (MutableList + MutableStateFlow)
 * Otomatik cleanup (1000+ kayıt varsa)
 * Flow-based reactive API
 * TODO: SQLDelight ile database persistence
 */

class LocationRepositoryImpl(private val mapper: GeoLocationEntityDbMapper, private val iDatabaseManager: IDatabaseManager): ILocationRepository {
    //region Field
    // In-memory storage (geçici - database ile değiştirilecek)
    private val locationHistory = mutableListOf<GpsLocation>()
    private val _lastLocation = MutableStateFlow<GpsLocation?>(null)
    private val _locationUpdates = MutableStateFlow<GpsLocation?>(null)
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun saveLocation(location: GpsLocation): Result<Unit> {
        return try {
            // Validasyon
            if (!location.isValid()) {
                return Result.Error(DomainException.BusinessRuleException(ErrorCode.INVALID_GPS_LOCATION))
            }

            // Listeye ekle
            locationHistory.add(location)

            // Flow'ları güncelle
            _lastLocation.value = location
            _locationUpdates.value = location

            // Eski verileri temizle (1000'den fazla kayıt varsa)
            if (locationHistory.size > 1000) {
                val toRemove = locationHistory.size - 1000
                locationHistory.subList(0, toRemove).clear()
            }
            val entity = mapper.fromGeoLocationFromDomain(location)
            iDatabaseManager.getSqlDriver().insert(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun getLastLocation(): GpsLocation? {
        return getBestRecentLocation()
    }

    override suspend fun getLocationHistory(limit: Int): List<GpsLocation> {
        val start = maxOf(0, locationHistory.size - limit)
        return locationHistory.subList(start, locationHistory.size).toList()
    }

    override suspend fun clearOldLocations(olderThan: Long): Result<Int> {
        return try {
            val sizeBefore = locationHistory.size
            locationHistory.removeAll { it.timestamp < olderThan }
            val removed = sizeBefore - locationHistory.size
            Result.Success(removed)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun getUnsyncedLocations(): List<GpsLocation> {
        return locationHistory.filter { !it.isSynced }
    }

    override suspend fun markAsSynced(locationIds: List<String>): Result<Unit> {
        return try {
            locationHistory.forEachIndexed { index, location ->
                if (location.id in locationIds) {
                    locationHistory[index] = location.copy(isSynced = true)
                }
            }
            val whereCondition = "WHERE GpsGuId IN (${locationIds.joinToString(
                separator = ",",
                prefix = "",
                postfix = ""
            ) { "'$it'" }})"
            val sqlString = "UPDATE GeoLocationEntity  SET SyncStatus = ${SyncStatusType.SUCCESS.ordinal} $whereCondition"
            iDatabaseManager.getSqlDriver().rawExecute(sqlString)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun getTimeSinceLastGps(): Duration {
        val lastLocation = getLastLocation()
        return if (lastLocation != null) {
            val currentTime = now()
            (currentTime - lastLocation.timestamp).milliseconds
        } else {
            Duration.INFINITE
        }
    }

    override suspend fun loadNotSycGpslist(): Result<Unit> {
        return try {
            val list = iDatabaseManager.getSqlDriver().select<GeoLocationEntity> {
                where {
                    criteria("SyncStatus", In = listOf(SyncStatusType.IDLE, SyncStatusType.FAILED, SyncStatusType.WAITING))
                }
            }.toList().map {
                mapper.fromGeoLocationToDomain(it)
            }

            val existingIds = this.locationHistory.map { it.id }.toSet()
            val newItems = list.filter { it.id !in existingIds }
            this.locationHistory.addAll(newItems)
            Result.Success(Unit)
        }catch (ex: Exception){
            Result.Error(DomainException.UnknownException(cause = ex))
        }
    }

    override fun calculateDistance(from: GpsLocation, to: GpsLocation): Double {
        return DistanceCalculator.calculateDistance(from, to)
    }

    override fun observeLastLocation(): Flow<GpsLocation?> {
        return _lastLocation.asStateFlow()
    }

    override fun observeLocationUpdates(): Flow<GpsLocation> {
        return flow {
            _locationUpdates.collect { location ->
                location?.let { emit(it) }
            }
        }
    }

    //endregion

    //region Private Method
    private fun getBestRecentLocation(maxAgeMillis: Long = 30_000): GpsLocation? {
        val now = now()
        val lastTime = now - maxAgeMillis

        // Son 30 saniyedeki GPS'leri filtrele
        val recentLocations = locationHistory.filter { location ->
            location.timestamp >= lastTime
        }

        if (recentLocations.isEmpty()) return null

        // En iyi accuracy'ye sahip olanı bul
        return recentLocations.minByOrNull { location ->
            val ageSeconds = (now - location.timestamp) / 1000.0
            val accuracyPenalty = location.accuracy
            val agePenalty = ageSeconds * 2

            accuracyPenalty + agePenalty
        }
    }
    //endregion
}
