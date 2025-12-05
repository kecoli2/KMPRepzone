package com.repzone.network.dto

import com.repzone.core.enums.StateType
import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class SyncCustomerProductDistributionDto(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null,
    val organizationId: Int,
    val productSalesDistributionListId: Int,
    val productSalesReturnDistributionListId: Int,
    val priceSalesDistributionListId: Int? = null,
    val priceSalesReturnDistributionListId: Int? = null,
    val priceSalesDamagedListId: Int? = null,
    val pricePurchaseReturnDamagedListId: Int? = null
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
