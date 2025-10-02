package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class MobileProductDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("State")
    val state: Int,
    @SerialName("Sku")
    val sku: String? = null,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Tags")
    val tags: String? = null,
    @SerialName("BrandId")
    val brandId: Int? = null,
    @SerialName("BrandName")
    val brandName: String? = null,
    @SerialName("DisplayOrder")
    val displayOrder: Int? = null,
    @SerialName("OrderQuantityFactor")
    val orderQuantityFactor: Int? = null,
    @SerialName("MinimumOrderQuantity")
    val minimumOrderQuantity: Int? = null,
    @SerialName("MaximumOrderQuantity")
    val maximumOrderQuantity: Int? = null,
    @SerialName("GroupId")
    val groupId: Int? = null,
    @SerialName("GroupName")
    val groupName: String? = null,
    @SerialName("PhotoPath")
    val photoPath: String? = null,
    @SerialName("Vat")
    val vat: Double? = null,
    @SerialName("OrganizationId")
    val organizationId: Int? = null,
    @SerialName("OrganizationIds")
    val organizationIds: String? = null,
    @SerialName("Units")
    val units: List<ServiceProductUnitDto> = emptyList(),
    @SerialName("Color")
    val color: String? = null,
    @SerialName("BrandPhotoPath")
    val brandPhotoPath: String? = null,
    @SerialName("GroupPhotoPath")
    val groupPhotoPath: String? = null,
    @SerialName("Parameters")
    val parameters: List<MobileProductParameterDto> = emptyList(),
    @SerialName("ModificationDateUtc")
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null, // ISO 8601 format string olarak
    @SerialName("CloseToSales")
    val closeToSales: Boolean? = null,
    @SerialName("CloseToReturns")
    val closeToReturns: Boolean? = null
)