package com.repzone.network.dto

import com.repzone.core.enums.CustomFieldDataType
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class PackageCustomFieldProductDto(
    val id: Int,
    val productId: Int,
    val fieldName: String? = null,
    @Serializable(with = CustomFieldDataType.Companion.Serializer::class)
    val dataType: CustomFieldDataType? = null,
    val itemList: String? = null,
    val stringMax: Int? = null,
    val integerMax: Int? = null,
    val integerMin: Int? = null,
    @Serializable(with = InstantSerializer::class)
    val dateMin: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val dateMax: Instant? = null,
    val decimalMin: Double? = null,
    val decimalMax: Double? = null,
    val mandatory: Boolean,
    val order: Int,
    val defaultValue: String? = null,
    val description: String? = null,
    val value: String? = null,
    val packageId: Int? = null,
    val packageName: String? = null
)
