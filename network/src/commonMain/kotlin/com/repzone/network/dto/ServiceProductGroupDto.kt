package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class ServiceProductGroupDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("State")
    val state: Int,
    @SerialName("RecordDateUtc")
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    @SerialName("ModificationDateUtc")
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @SerialName("ParentId")
    val parentId: Int? = null,
    @SerialName("OrganizationId")
    val organizationId: Int? = null,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Shared")
    val shared: Boolean,
    @SerialName("IconIndex")
    val iconIndex: Int,
    @SerialName("PhotoPath")
    val photoPath: String? = null
)