package com.repzone.domain.model.forms

import com.repzone.core.enums.FormDocumentType
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormModel(
    override var documentUniqueId: String? = null,
    override var formId: Int = 0,
    override var schemaName: String? = null,
    override var name: String? = null,
    override var formDocumentType: FormDocumentType? = null,
    override var version: String? = null,
    override var visitId: Int = 0,
    override var visitUniqueId: String? = null,
    override var tenantId: Int = 0,
    override var organizationId: Int = 0,
    override var customerId: Int = 0,
    override var representativeId: Int = 0,
    override var recordDateUtc: Instant = now().toInstant(),
    override var formUniqueId: String?= null,
    override var longitude: Double = 0.0,
    override var latitude: Double = 0.0,
    override var rowControls: List<RowControl> = emptyList(),
    override var embeddedProperties: List<ColumnControl> = emptyList(),
    override var externalObjects: List<ColumnControl> = emptyList()
) : BaseFormModel
