package com.repzone.domain.model

import com.repzone.core.enums.AddressType

data class SyncAddressModel(
    val id: Long,
    val addressName: String?,
    val addressType: AddressType,
    val city: String?,
    val contact: String?,
    val country: String?,
    val customerId: Long?,
    val district: String?,
    val faxNumber: String?,
    val latitude: Double?,
    val longitude: Double?,
    val phoneNumber: String?,
    val state: Long?,
    val street: String?,
    val street2: String?,
)