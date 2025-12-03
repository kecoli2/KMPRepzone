package com.repzone.network.dto

import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class SyncMandatoryFormDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null,
    val formId: Int,
    val version: String? = null,
    val customerId: Int,
    val customerName: String? = null
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
