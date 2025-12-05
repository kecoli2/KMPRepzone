package com.repzone.domain.document.service

import com.repzone.core.model.module.base.IDocumentBaseParameters
import com.repzone.domain.document.IDocumentParameters
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository

class DocumentParametersImpl(private val iDocumentMapRepository: IDocumentMapRepository,
                             private val iMobileModuleParameterRepository: IMobileModuleParameterRepository): IDocumentParameters {
    //region Field
    private var documentMapModel: SyncDocumentMapModel? = null
    private var documentParameters: IDocumentBaseParameters? = null
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override suspend fun preLoadDocumentParameters(documentType: DocumentType, customer: SyncCustomerModel, documentId: Long) {
        documentMapModel = iDocumentMapRepository.get(documentId.toInt(), customer.organizationId?.toInt() ?: 0)
        documentParameters = when(documentType){
            DocumentType.ORDER -> iMobileModuleParameterRepository.getOrdersParameters()!!
            DocumentType.INVOICE -> iMobileModuleParameterRepository.getInvoiceEInvoceParameters()!!
            DocumentType.WAYBILL -> iMobileModuleParameterRepository.getDispatchesParameters()!!
        }

    }

    override fun getDocumentMapModel(): SyncDocumentMapModel {
        return documentMapModel!!
    }

    override fun getDocumentParameters(): IDocumentBaseParameters {
        return documentParameters!!
    }
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
}