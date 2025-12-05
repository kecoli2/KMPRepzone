package com.repzone.core.model.module.parameters

import com.repzone.core.enums.AutoFilterByStock
import com.repzone.core.enums.OnOf
import com.repzone.core.enums.OrganizationType
import com.repzone.core.enums.ShowDocumentLabelsOrderType
import com.repzone.core.enums.ShowDocumentLabelsType
import com.repzone.core.enums.ShowPaytermSelectionType
import com.repzone.core.enums.StockEntryForm
import com.repzone.core.enums.UserSelectionType
import com.repzone.core.model.module.base.IDocumentBaseParameters
import com.repzone.core.model.module.base.IModuleParametersBase

data class InvoiceEInvoiceParameters(
    override val allowDraft: Boolean,
    override val allowStockEntry: OnOf,
    override val stockEntryFormId: StockEntryForm? = null,
    override val askReasonCode: OnOf,
    override val printingAllowed: Boolean,
    override val keepDraftAfterProcess: Boolean,
    override val allowWarehouseSelection: Boolean,
    override val showDocumentLabels: ShowDocumentLabelsOrderType,
    override val formOrganizationSelectionOptions: OrganizationType,
    override val showProductOrderPending: Boolean,
    override val showPaytermSelection: ShowPaytermSelectionType,
    override val showWarehouseSelection: UserSelectionType,
    override val dayToShip: Int,
    override val autoFilterByStock: AutoFilterByStock,
    override val showProductOrderAvailableStock: Boolean,
    override val showProductOrderTransitStock: Boolean,
    override val isActive: Boolean
): IDocumentBaseParameters
