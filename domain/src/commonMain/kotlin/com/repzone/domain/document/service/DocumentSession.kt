package com.repzone.domain.document.service

import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DocumentType

/**
 * Belge session yöneticisi implementation
 */
class DocumentSession(
    private val promotionEngine: IPromotionEngine,
    private val stockValidator: StockValidator,
    private val lineCalculator: LineDiscountCalculator
) : IDocumentSession {
    
    private var _documentManager: IDocumentManager? = null
    

    override fun start(type: DocumentType): IDocumentManager {
        if (_documentManager == null) {
            _documentManager = DocumentManager(
                documentType = type,
                promotionEngine = promotionEngine,
                stockValidator = stockValidator,
                lineCalculator = lineCalculator
            )
        }
        return _documentManager!!
    }

    override fun current(): IDocumentManager {
        return _documentManager 
            ?: throw IllegalStateException("No active document session")
    }
    
    override fun isActive(): Boolean = _documentManager != null
    
    override fun clear() {
        _documentManager?.clear()
        _documentManager = null
    }
}
