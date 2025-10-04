package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
enum class AddressType {
    @SerialName("0")
    MAIL,
    @SerialName("1")
    EMPTY1,
    @SerialName("2")
    BILL,
    @SerialName("3")
    EMPTY2,
    @SerialName("4")
    SHIPPING
}