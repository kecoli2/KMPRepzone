package com.repzone.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncProductModel(
    @SerialName("Id")
    val id: Long,
    @SerialName("BrandId")
    val brandId: Long?,
    @SerialName("BrandName")
    val brandName: String?,
    @SerialName("BrandPhotoPath")
    val brandPhotoPath: String?,
    @SerialName("CloseToReturns")
    val closeToReturns: Boolean,
    @SerialName("CloseToSales")
    val closeToSales: Boolean,
    @SerialName("Color")
    val color: String?,
    @SerialName("Description")
    val description: String?,
    @SerialName("DisplayOrder")
    val displayOrder: Long?,
    @SerialName("GroupId")
    val groupId: Long?,
    @SerialName("GroupName")
    val groupName: String?,
    @SerialName("GroupPhotoPath")
    val groupPhotoPath: String?,
    val isVisible: Boolean?,
    val manufacturerId: Long?,
    @SerialName("MaximumOrderQuantity")
    val maximumOrderQuantity: Long?,
    @SerialName("MinimumOrderQuantity")
    val minimumOrderQuantity: Long?,
    @SerialName("ModificationDateUtc")
    val modificationDateUtc: Long?, // ISO 8601 format string olarak
    @SerialName("Name")
    val name: String?,
    @SerialName("OrderQuantityFactor")
    val orderQuantityFactor: Long?,
    @SerialName("OrganizationId")
    val organizationId: Long?,
    @SerialName("OrganizationIds")
    val organizationIds: String?,
    @SerialName("PhotoPath")
    val photoPath: String?,
    val recordDateUtc: Long?,
    val shared: Long?,
    @SerialName("Sku")
    val sku: String?,
    @SerialName("State")
    val state: Long?,
    @SerialName("Tags")
    val tags: String?,
    val tenantId: Long?,
    @SerialName("Vat")
    val vat: Double?,

/*

    @SerialName("Parameters")
    val parameters: List<MobileProductParameter>,

    @SerialName("Units")
    val units: List<ServiceProductUnitModel>,
*/


)
