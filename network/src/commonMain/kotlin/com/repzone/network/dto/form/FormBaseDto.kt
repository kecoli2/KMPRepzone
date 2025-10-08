package com.repzone.network.dto.form

import com.repzone.core.enums.FormDocumentType
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormBaseDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,

    /**
     * Formun şema adı yani kodudur
     */
    val schemaName: String? = null,

    /**
     * Formun cihazlarda görünecek ismidir
     */
    val name: String? = null,

    /**
     * Formun versiyonudur
     */
    val version: String? = null,

    /**
     * Belge tipidir = Form/ anket /
     */
    @Serializable(with = FormDocumentType.Companion.Serializer::class)
    val formDocumentType: FormDocumentType = FormDocumentType.FORM,

    val tenantId: Int? = null,
    val organizationId: Int? = null,
    val organizationIds: String? = null,

    /**
     * Boylam
     */
    val longitude: Double? = null,

    /**
     * Enlem
     */
    val latitude: Double? = null,

    /**
     * Tag bilgisi
     */
    val tags: String? = null,
    val representativeTags: String? = null,
    val customerTags: String? = null,
    val targetType: Int? = null,
    val targetCount: Int? = null,
    val embeddedProperties: List<FormEmbeddedPropertyDto> = emptyList(),
    val formAttributes: List<FormAttributeDto> = emptyList(),
    val formRows: List<FormRowDto> = emptyList()
)