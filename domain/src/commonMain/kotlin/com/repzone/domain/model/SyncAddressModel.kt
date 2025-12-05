package com.repzone.domain.model

import com.repzone.core.enums.AddressType
import com.repzone.core.enums.StateType

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
    val state: StateType,
    val street: String?,
    val street2: String?,
)