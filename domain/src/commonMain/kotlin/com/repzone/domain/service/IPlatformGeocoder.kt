package com.repzone.domain.service

import com.repzone.domain.model.gps.GeocodingAddress

interface IPlatformGeocoder {
    suspend fun getAddress(latitude: Double, longitude: Double): GeocodingAddress?
    fun isAvailable(): Boolean
}