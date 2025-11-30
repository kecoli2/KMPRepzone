package com.repzone.network.dto

import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class SyncPaymentPlanDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant?,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant?,
    val tenantId: Int,
    val name: String,
    val isDefault: Boolean,
    val organizationId: Int,
    val ids: String?,
    val code: String?,
    val maturityDays: Int
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
