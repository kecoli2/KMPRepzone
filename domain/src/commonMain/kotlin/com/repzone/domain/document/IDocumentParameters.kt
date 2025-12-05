package com.repzone.domain.document

import com.repzone.core.model.module.base.IDocumentBaseParameters
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncDocumentMapModel

interface IDocumentParameters {
    suspend fun preLoadDocumentParameters(documentType: DocumentType,customer: SyncCustomerModel, documentId: Long)
    fun getDocumentMapModel(): SyncDocumentMapModel
    fun getDocumentParameters(): IDocumentBaseParameters
}
