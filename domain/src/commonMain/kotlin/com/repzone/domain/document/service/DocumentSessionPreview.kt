package com.repzone.domain.document.service

import com.repzone.core.interfaces.IUserSession
import com.repzone.domain.common.Result
import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IDistributionRepository
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IProductRepository
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DocumentSessionPreview(private val promotionEngine: IPromotionEngine,
                             private val stockValidator: StockValidator,
                             private val lineCalculator: LineDiscountCalculator,
                             private val customerRepository: ICustomerRepository,
                             private val documentMapRepository: IDocumentMapRepository,
                             private val distributionRepository: IDistributionRepository,
                             private val userSession: IUserSession,
                             private val productRepository: IProductRepository) : IDocumentSession {

    //region Field
    private var _documentManager: IDocumentManager? = null
    //endregion

    //region Public Method
    override suspend fun start(type: DocumentType, customerId: Long, documentId: Long):Result<IDocumentManager> {
        if (_documentManager == null) {
            _documentManager = DocumentManager(
                documentType = type,
                promotionEngine = promotionEngine,
                stockValidator = stockValidator,
                lineCalculator = lineCalculator,
                iCustomerRepository = customerRepository,
                iDocumentMapRepository = documentMapRepository,
                iDistributionRepository = distributionRepository,
                iUserSession = userSession,
                iProductRepository = productRepository
            )
            return _documentManager!!.setMasterValues(customerId, documentId)

        }
        return Result.Success(_documentManager!!)
    }

    override fun current(): IDocumentManager {
        _documentManager = DocumentManager(
            documentType = DocumentType.ORDER,
            promotionEngine = promotionEngine,
            stockValidator = stockValidator,
            lineCalculator = lineCalculator,
            iCustomerRepository = customerRepository,
            iDocumentMapRepository = documentMapRepository,
            iDistributionRepository = distributionRepository,
            iUserSession = userSession,
            iProductRepository = productRepository
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