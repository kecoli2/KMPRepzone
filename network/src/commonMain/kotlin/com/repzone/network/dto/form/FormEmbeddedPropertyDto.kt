package com.repzone.network.dto.form

import com.repzone.core.enums.StateType
import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormEmbeddedPropertyDto (
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null,
    var formId: Int = 0,
    var controlName: String? = null,
    var controlType: String? = null,
    var controlText: String? = null,
    var caption: String? = null,
    var returnValue: String? = null,
    var maxMbSize: Int = 0,
    var maxFileCount: Int = 0,
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}