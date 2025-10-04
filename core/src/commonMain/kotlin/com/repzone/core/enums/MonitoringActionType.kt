package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
enum class MonitoringActionType {
    @SerialName("0")
    DONOTHING,
    @SerialName("1")
    STOPACTION,
    @SerialName("2")
    GIVEWARNING
}