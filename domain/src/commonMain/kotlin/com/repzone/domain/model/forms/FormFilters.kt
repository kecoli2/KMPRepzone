package com.repzone.domain.model.forms

import com.repzone.core.enums.StateType
import com.repzone.core.model.base.IBaseModel
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormFilters(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    override val modificationDateUtc: Instant?,
    override val recordDateUtc: Instant?,
    var attribute: FormAttributeObject? = null,
    var filterType: String? = null,
    var source: String? = null,
    var formRowId: Int = 0,
    var operator: String? = null,
    var formRow: FormRow? = null,
) : IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
