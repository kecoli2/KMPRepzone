package com.repzone.network.dto.form

import com.repzone.core.enums.EntityModelType
import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormRowDto(
    override val id: Int,
    override val state: Int,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null,
    val formId: Int = 0,
    val controlName: String? = null,
    val viewType: String? = null,
    val order: Int = 0,
    val caption: String? = null,
    @Serializable(with = EntityModelType.Companion.Serializer::class)
    val dataSourceType: EntityModelType? = null,
    val controlData: String? = null,
    val dataId: String? = null,
    val dataValue: String? = null,
    val rowColumns: List<FormRowColumnDto> = emptyList(),
    val filters: List<FormFiltersDto> = emptyList(),
    @kotlinx.serialization.Transient
    val form: FormBaseDto? = null,
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}