package com.repzone.network.dto

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class CrmPriceListParameterDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    val entityType: CrmParameterEntityType = CrmParameterEntityType.CUSTOMER,
    val entityId: Int? = null,
    val paymentPlanId: Int? = null,
    val salesPriceListId: Int? = null,
    val salesReturnPriceListId: Int? = null,
    val purchasePriceListId: Int? = null,
    val purchaseReturnPriceListId: Int? = null,
    val priceSalesDamagedListId: Int? = null,
    val pricePurchaseReturnDamagedListId: Int? = null,
    val organizationId: Int? = null
)