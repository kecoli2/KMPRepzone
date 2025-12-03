package com.repzone.network.dto

import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class SyncRepresentativeProductDistributionDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant?,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant?,
    val groupId: Int,
    val salesDistributionListId: Int,
    val salesReturnDistributionListId: Int,
    val groupSalesDistributionListId: Int,
    val groupSalesReturnDistributionListId: Int
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
