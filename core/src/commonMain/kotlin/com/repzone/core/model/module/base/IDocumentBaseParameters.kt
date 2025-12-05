package com.repzone.core.model.module.base

import com.repzone.core.enums.AutoFilterByStock
import com.repzone.core.enums.OnOf
import com.repzone.core.enums.OrganizationType
import com.repzone.core.enums.ShowDocumentLabelsOrderType
import com.repzone.core.enums.ShowPaytermSelectionType
import com.repzone.core.enums.StockEntryForm
import com.repzone.core.enums.UserSelectionType

interface IDocumentBaseParameters : IModuleParametersBase {
    val allowDraft: Boolean
    val allowStockEntry: OnOf
    val stockEntryFormId: StockEntryForm?
    val askReasonCode: OnOf
    val printingAllowed: Boolean
    val keepDraftAfterProcess: Boolean
    val allowWarehouseSelection: Boolean
    val showDocumentLabels: ShowDocumentLabelsOrderType
    val formOrganizationSelectionOptions: OrganizationType
    val showProductOrderPending: Boolean
    val showPaytermSelection: ShowPaytermSelectionType
    val showWarehouseSelection: UserSelectionType
    val dayToShip: Int
    val autoFilterByStock: AutoFilterByStock
    val showProductOrderAvailableStock: Boolean
    val showProductOrderTransitStock: Boolean
}