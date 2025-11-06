package com.repzone.network.dto

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DocumentMapDocumentOrganizationDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    var organizationId: Int? = null,
    var documentTypeId: Int? = null,
    var minMaxControl: Boolean = false,
    var lang: String? = null,
    var logoSelection: Int? = null,
    var logoPathUrl: String? = null,
    var printQrCode: Boolean = false,
    var useFinancialLogo: Boolean = false,
    var documentHeader: String? = null,
    var uniqueIdCaption: String? = null,
    var note: String? = null,
    var printerTemplatePath: String? = null,
    var printerTemplateAbsolutePath: String? = null
)
