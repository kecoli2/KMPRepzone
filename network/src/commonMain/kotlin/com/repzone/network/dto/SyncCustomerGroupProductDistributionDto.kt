package com.repzone.network.dto

import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class SyncCustomerGroupProductDistributionDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant?,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant?,
    val organizationId: Int,
    val productSalesDistributionListId: Int,
    val productSalesReturnDistributionListId: Int,
    val priceSalesDistributionListId: Int,
    val priceSalesReturnDistributionListId: Int,
    val priceSalesDamagedListId: Int?,
    val pricePurchaseReturnDamagedListId: Int?,
    val mustStockListId: Int

): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
