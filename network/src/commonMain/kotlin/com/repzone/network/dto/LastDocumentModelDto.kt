package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class LastDocumentModelDto(
    val docId: Int = 0,
    val docNumber: String = "",
    @Serializable(with = InstantSerializer::class)
    val docDate: Instant? = null
)