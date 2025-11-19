package com.repzone.mobile.impl

import android.content.Context
import android.location.Geocoder
import com.repzone.domain.model.gps.GeocodingAddress
import com.repzone.domain.service.IPlatformGeocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AndroidGeocoderImpl(private val context: Context) : IPlatformGeocoder {
    //region Field
    private val geocoder = Geocoder(context, Locale.getDefault())
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getAddress(latitude: Double, longitude: Double): GeocodingAddress? = withContext(Dispatchers.IO) {
        try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            addresses?.firstOrNull()?.let { androidAddress ->
                GeocodingAddress(
                    street = androidAddress.thoroughfare,
                    streetNumber = androidAddress.subThoroughfare,
                    district = androidAddress.subAdminArea,
                    city = androidAddress.locality,
                    state = androidAddress.adminArea,
                    country = androidAddress.countryName,
                    postalCode = androidAddress.postalCode,
                    fullAddress = androidAddress.getAddressLine(0) ?: buildFullAddress(androidAddress)
                )
            }
        } catch (e: Exception) {
            println("AndroidGeocoder error: ${e.message}")
            null
        }
    }

    override fun isAvailable(): Boolean {
        return Geocoder.isPresent()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun buildFullAddress(address: android.location.Address): String {
        val parts = mutableListOf<String>()

        address.thoroughfare?.let { parts.add(it) }
        address.subThoroughfare?.let { parts.add(it) }
        address.subAdminArea?.let { parts.add(it) }
        address.locality?.let { parts.add(it) }

        return parts.joinToString(", ")
    }
    //endregion



}