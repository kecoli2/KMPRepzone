package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class DynamicPageReportDto (
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    val name: String? = null,
    val category: String? = null,
    val code: String? = null,
    val description: String? = null,
    val requested: Boolean,
    val quickAccessShow: Boolean,
    val quickAccessOrder: Int? = null
)