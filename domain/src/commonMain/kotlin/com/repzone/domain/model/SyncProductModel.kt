package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductModel(
    val id: Long,
    val brandId: Long?,
    val brandName: String?,
    val brandPhotoPath: String?,
    val closeToReturns: Boolean,
    val closeToSales: Boolean,
    val color: String?,
    val description: String?,
    val displayOrder: Long?,
    val groupId: Long?,
    val groupName: String?,
    val groupPhotoPath: String?,
    val isVisible: Boolean?,
    val manufacturerId: Long?,
    val maximumOrderQuantity: Long?,
    val minimumOrderQuantity: Long?,
    val modificationDateUtc: Long?, // ISO 8601 format string olarak
    val name: String?,
    val orderQuantityFactor: Long?,
    val organizationId: Long?,
    val organizationIds: String?,
    val photoPath: String?,
    val recordDateUtc: Long?,
    val shared: Long?,
    val sku: String?,
    val state: StateType,
    val tags: String?,
    val tenantId: Long?,
    val vat: Double?,
/*

    @SerialName("Parameters")
    val parameters: List<MobileProductParameter>,

    @SerialName("Units")
    val units: List<ServiceProductUnitModel>,
*/


)
