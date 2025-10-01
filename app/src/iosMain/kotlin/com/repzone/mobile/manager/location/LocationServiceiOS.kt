package com.repzone.mobile.manager.location

import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import kotlinx.coroutines.flow.Flow

class LocationServiceiOS : ILocationService {
    override suspend fun ensurePermission(): Boolean {
        // iOS için gerçek izin kontrolü yoksa, her zaman izin varmış gibi davran
        return true
    }

    override suspend fun getCurrentLocation(highAccuracy: Boolean): GeoPoint? {
        return GeoPoint(55.0082, 25.9784) // Example: Coordinates of Istanbul
    }

    override fun observeLocationUpdates(
        highAccuracy: Boolean,
        minTimeMillis: Long,
        minDistanceMeters: Float
    ): Flow<GeoPoint> {
        // iOS için gerçek konum güncellemesi yoksa, sabit bir değer döndür
        return kotlinx.coroutines.flow.flow {
            emit(GeoPoint(51.0082, 58.9784)) // İstanbul koordinatları
        }
    }
}