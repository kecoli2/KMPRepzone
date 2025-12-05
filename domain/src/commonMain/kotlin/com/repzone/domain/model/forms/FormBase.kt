package com.repzone.domain.model.forms

import com.repzone.core.enums.FormDocumentType
import com.repzone.core.enums.FormOperationType
import com.repzone.core.enums.FormVisibleOption
import com.repzone.core.enums.StateType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.model.base.IBaseModel
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormBase(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    override val modificationDateUtc: Instant? = null,
    override val recordDateUtc: Instant? = null,
    /**
     * Formun şema adı yani kodudur
     */
    var schemaName: String? = null,

    /**
     * Formun cihazlarda görünecek ismidir
      */
    var name: String? = null,

    /**
     * Formun versiyonudur
     */
    var version: String? = null,

    /**
     * Belge tipidir = Form - anket
      */
    var formDocumentType: FormDocumentType? = null,

    var tenantId: Int = 0,
    var organizationId: Int = 0,
    var organizationIds: String? = null,

    /**
     * Boylam
      */
    var longitude: Double? = null,

    /**
     * Enlem
      */
    var latitude: Double? = null,

    /**
     * Tag bilgisi
      */
    var tags: String? = null,
    var representativeTags: String? = null,
    var customerTags: String? = null,
    var targetType: Int = 0,
    var targetCount: Int? = null,
    var embeddedProperties: List<FormEmbeddedProperty> = emptyList(),
    var formAttributes: List<FormAttribute> = emptyList(),
    var formRows: List<FormRow> = emptyList(),
    var iconIndex: Int = 0,
    var beginDate: Instant? = null,
    var endDate: Instant? = null,
    var isMandatory: Boolean = false,
    var interval: TaskRepeatInterval = TaskRepeatInterval.NONE,
    var createdByTask: Boolean = false,
    var displayOrder: Int = 0,
    var visibleOption: FormVisibleOption? = null,
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
