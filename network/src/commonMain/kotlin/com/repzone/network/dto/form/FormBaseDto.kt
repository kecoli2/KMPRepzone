package com.repzone.network.dto.form

import com.repzone.core.enums.FormDocumentType
import com.repzone.core.enums.FormOperationType
import com.repzone.core.enums.FormVisibleOption
import com.repzone.core.enums.MonitoringActionType
import com.repzone.core.enums.StateType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormBaseDto(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant? = null,
    var schemaName: String? = null,
    var name: String? = null,
    var version: String? = null,
    @Serializable(with = FormDocumentType.Companion.Serializer::class)
    var formDocumentType: FormDocumentType? = null,
    var tenantId: Int = 0,
    var organizationId: Int = 0,
    var organizationIds: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
    var tags: String? = null,
    var representativeTags: String? = null,
    var customerTags: String? = null,
    var targetType: Int = 0,
    var targetCount: Int? = null,
    var embeddedProperties: List<FormEmbeddedPropertyDto> = emptyList(),
    var formAttributes: List<FormAttributeDto> = emptyList(),
    var formRows: List<FormRowDto> = emptyList(),
    var iconIndex: Int = 0,
    @Serializable(with = InstantSerializer::class)
    var beginDate: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    var endDate: Instant? = null,
    var isMandatory: Boolean = false,
    @Serializable(with = TaskRepeatInterval.Companion.Serializer::class)
    var interval: TaskRepeatInterval = TaskRepeatInterval.NONE,
    var createdByTask: Boolean = false,
    var displayOrder: Int = 0,
    @Serializable(with = FormVisibleOption.Companion.Serializer::class)
    var visibleOption: FormVisibleOption? = null,
    @Serializable(with = FormOperationType.Companion.Serializer::class)
    var operationType: FormOperationType? = null,
    var customerCategories: List<Int> = emptyList(),
    var customerChannels: List<Int> = emptyList(),
    var customerClasses: List<Int> = emptyList(),
    var customerSegments: List<Int> = emptyList(),
    var customerGroups: List<Int> = emptyList()
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}