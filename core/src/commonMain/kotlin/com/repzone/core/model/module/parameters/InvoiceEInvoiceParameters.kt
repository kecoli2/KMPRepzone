package com.repzone.core.model.module.parameters

import com.repzone.core.enums.ShowDocumentLabelsType
import com.repzone.core.model.module.base.IModuleParametersBase

data class InvoiceEInvoiceParameters(
    override val isActive: Boolean,
    val printingAllowed: Boolean,
    val showDocumentLabels: ShowDocumentLabelsType,

): IModuleParametersBase
