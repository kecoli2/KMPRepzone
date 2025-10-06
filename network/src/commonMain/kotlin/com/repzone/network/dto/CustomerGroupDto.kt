package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class CustomerGroupDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    val name: String? = null,
    val parentId: Int? = null,
    val organizationId: Int? = null,
    val shared: Boolean,
    val isDefault: Boolean,
    val iconIndex: Int? = null
)