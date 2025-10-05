package com.repzone.network.dto

import com.repzone.core.enums.AddressType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobileAddressDto(
    val id: Int,
    val state: Int,
    val name: String? = null,
    val responsible: String? = null,
    val phone: String? = null,
    val fax: String? = null,
    @Serializable(with = AddressType.Companion.Serializer::class)
    val type: AddressType,
    @SerialName("Streeet")
    val street: String? = null,
    val district: String? = null,
    val city: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val street2: String? = null,
    val country: String? = null
)