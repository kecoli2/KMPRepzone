package com.repzone.domain.repository

import com.repzone.domain.model.gps.GpsLocation
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface ILocationRepository {
    /**
     * Veri Operasyonları
     */
    suspend fun saveLocation(location: GpsLocation)
    suspend fun getLastLocation(): GpsLocation?
    suspend fun getLocationHistory(limit: Int): List<GpsLocation>
    suspend fun clearOldLocations(olderThan: Long)

    /**
     * Metadata bilgileri
     */
    suspend fun getTimeSinceLastGps(): Duration
    suspend fun calculateDistance(from: GpsLocation, to: GpsLocation): Double

    /**
     * Observe islemleri
     */
    fun observeLastLocation(): Flow<GpsLocation?>
}