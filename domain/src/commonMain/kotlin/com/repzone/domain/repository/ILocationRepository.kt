package com.repzone.domain.repository

import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsLocation
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface ILocationRepository {
    suspend fun saveLocation(location: GpsLocation): Result<Unit>
    suspend fun getLastLocation(): GpsLocation?
    suspend fun getLocationHistory(limit: Int = 100): List<GpsLocation>
    suspend fun clearOldLocations(olderThan: Long): Result<Int>
    suspend fun getUnsyncedLocations(): List<GpsLocation>
    suspend fun markAsSynced(locationIds: List<String>): Result<Unit>
    suspend fun getTimeSinceLastGps(): Duration
    suspend fun loadNotSycGpslist(): Result<Unit>
    fun calculateDistance(from: GpsLocation, to: GpsLocation): Double
    fun observeLastLocation(): Flow<GpsLocation?>
    fun observeLocationUpdates(): Flow<GpsLocation>
}