package com.repzone.mobile.manager.location

import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import kotlinx.coroutines.flow.Flow

class LocationServiceiOS : ILocationService {
    override suspend fun ensurePermission(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentLocation(highAccuracy: Boolean): GeoPoint? {
        TODO("Not yet implemented")
    }

    override fun observeLocationUpdates(
        highAccuracy: Boolean,
        minTimeMillis: Long,
        minDistanceMeters: Float
    ): Flow<GeoPoint> {
        TODO("Not yet implemented")
    }
}