package com.repzone.mobile.impl

import com.repzone.domain.model.gps.GeocodingAddress
import com.repzone.domain.service.IPlatformGeocoder
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import kotlin.coroutines.resume

class IosGeocoderImpl: IPlatformGeocoder {
    //region Field
    private val geocoder = CLGeocoder()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun isAvailable(): Boolean {
        return true
    }

    override suspend fun getAddress(latitude: Double, longitude: Double): GeocodingAddress? = suspendCancellableCoroutine { continuation ->
        val location = CLLocation(latitude = latitude, longitude = longitude)

        geocoder.reverseGeocodeLocation(location) { placemarks, error ->
            if (error != null) {
                continuation.resume(null)
                return@reverseGeocodeLocation
            }

            @Suppress("UNCHECKED_CAST")
            val placemark = (placemarks as? List<CLPlacemark>)?.firstOrNull()

            if (placemark != null) {
                val address = GeocodingAddress(
                    street = placemark.thoroughfare,
                    streetNumber = placemark.subThoroughfare,
                    district = placemark.subLocality,
                    city = placemark.locality,
                    state = placemark.administrativeArea,
                    country = placemark.country,
                    postalCode = placemark.postalCode,
                    fullAddress = buildFullAddress(placemark)
                )
                continuation.resume(address)
            } else {
                continuation.resume(null)
            }
        }

        continuation.invokeOnCancellation {
            geocoder.cancelGeocode()
        }
    }
    //endregion

    //region Private Method
    private fun buildFullAddress(placemark: CLPlacemark): String {
        val parts = mutableListOf<String>()

        placemark.thoroughfare?.let { parts.add(it as String) }
        placemark.subThoroughfare?.let { parts.add(it as String) }
        placemark.subLocality?.let { parts.add(it as String) }
        placemark.locality?.let { parts.add(it as String) }

        return parts.joinToString(", ")
    }
    //endregion
}