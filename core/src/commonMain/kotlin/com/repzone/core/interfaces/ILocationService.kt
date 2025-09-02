package com.repzone.core.interfaces

import com.repzone.core.model.GeoPoint
import kotlinx.coroutines.flow.Flow

interface ILocationService {

    suspend fun ensurePermission(): Boolean
    /** Tek seferlik en iyi konumu döndürür (timeout/last known fallback uygulanabilir). */
    suspend fun getCurrentLocation(highAccuracy: Boolean = true): GeoPoint?

    /** Sürekli konum akışı (ör. 5-10sn’de bir). */
    fun observeLocationUpdates(highAccuracy: Boolean = false, minTimeMillis: Long = 10_000, minDistanceMeters: Float = 10f): Flow<GeoPoint>
}