package com.repzone.domain.model.gps

data class GeocodingAddress(
    val street: String?,
    val streetNumber: String?,
    val district: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postalCode: String?,
    val fullAddress: String
)
