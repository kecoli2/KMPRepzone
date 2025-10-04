package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
enum class PriceType {
    @SerialName("0")
    DEFAULT,
    @SerialName("1")
    SALES,
    @SerialName("2")
    RETURN,
    @SerialName("3")
    DAMAGERETURN
}