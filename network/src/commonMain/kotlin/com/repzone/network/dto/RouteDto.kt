package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class RouteDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    val appointmentDue: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val appointmentEnd: Instant? = null,
    val sprintId: Int,
    val customerId: Int,
    val description: String? = null,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null): ISyncBaseModel
