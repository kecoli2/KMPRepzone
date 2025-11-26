package com.repzone.domain.document.service

import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.CustomerItemModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DocumentSessionPreview(private val promotionEngine: IPromotionEngine,
                             private val stockValidator: StockValidator,
                             private val lineCalculator: LineDiscountCalculator) : IDocumentSession {

    //region Field
    private var _documentManager: IDocumentManager? = null
    //endregion

    //region Public Method
    override fun start(type: DocumentType, customerItem: CustomerItemModel, documentName: String): IDocumentManager {
        if (_documentManager == null) {
            _documentManager = DocumentManager(
                documentType = type,
                promotionEngine = promotionEngine,
                stockValidator = stockValidator,
                lineCalculator = lineCalculator,
                customer = customerItem,
                documentName = documentName
            )
        }
        return _documentManager!!
    }

    override fun current(): IDocumentManager {
        _documentManager = DocumentManager(
            documentType = DocumentType.ORDER,
            promotionEngine = promotionEngine,
            stockValidator = stockValidator,
            lineCalculator = lineCalculator,
            customer = CustomerItemModel(
                customerId = 1,
                visitId = 1,
                iconIndex = 0,
                appointmentId = 0,
                date = null,
                tagRaw = emptyList(),
                name = "Salih",
                customerCode = "123",
                customerGroupName = "sasas",
                address = "dsadsadsa",
                latitude = 0.0,
                longitude = 0.0,
                addressType = null,
                imageUri = null,
                parentCustomerId = null,
                endDate = null,
                customerBlocked = false,
                sprintId = null,
                dontShowDatePart = false,
                swipeEnabled = false,
                showCalendarInfo = false,
                showDisplayClock = false,
                displayOrder = null,
                showDisplayOrder = false,
                isECustomer = true,
                balance = 157.77,
                visitFinishDate = null,
                visitStartDate = null
            ),
            documentName = "deneme"
        )
        return _documentManager!!
    }

    override fun isActive(): Boolean = _documentManager != null

    override fun clear() {
        _documentManager?.clear()
        _documentManager = null
    }
    //endregion
}