package com.repzone.network.dto.form

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormFiltersDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    val attribute: FormAttributeObjectDto? = null,
    val filterType: String? = null,
    val source: String? = null,
    val formId: Int? = null,
    val operator: String? = null,
    @kotlinx.serialization.Transient
    val formRow: FormRowDto? = null
)
