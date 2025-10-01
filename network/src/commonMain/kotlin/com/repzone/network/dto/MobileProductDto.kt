package com.repzone.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobileProductDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("State")
    val state: Int,
    @SerialName("Sku")
    val sku: String?,
    @SerialName("Name")
    val name: String?,
    @SerialName("Description")
    val description: String?,
    @SerialName("Tags")
    val tags: String?,
    @SerialName("BrandId")
    val brandId: Int?,
    @SerialName("BrandName")
    val brandName: String?,
    @SerialName("DisplayOrder")
    val displayOrder: Int?,
    @SerialName("OrderQuantityFactor")
    val orderQuantityFactor: Int?,
    @SerialName("MinimumOrderQuantity")
    val minimumOrderQuantity: Int?,
    @SerialName("MaximumOrderQuantity")
    val maximumOrderQuantity: Int?,
    @SerialName("GroupId")
    val groupId: Int?,
    @SerialName("GroupName")
    val groupName: String?,
    @SerialName("PhotoPath")
    val photoPath: String?,
    @SerialName("Vat")
    val vat: Double?,
    @SerialName("OrganizationId")
    val organizationId: Int?,
    @SerialName("OrganizationIds")
    val organizationIds: String?,
    @SerialName("Units")
    val units: List<ServiceProductUnitDto>,
    @SerialName("Color")
    val color: String?,
    @SerialName("BrandPhotoPath")
    val brandPhotoPath: String?,
    @SerialName("GroupPhotoPath")
    val groupPhotoPath: String?,
    @SerialName("Parameters")
    val parameters: List<MobileProductParameterDto>,
    @SerialName("ModificationDateUtc")
    val modificationDateUtc: Long?, // ISO 8601 format string olarak
    @SerialName("CloseToSales")
    val closeToSales: Boolean?,
    @SerialName("CloseToReturns")
    val closeToReturns: Boolean?
)