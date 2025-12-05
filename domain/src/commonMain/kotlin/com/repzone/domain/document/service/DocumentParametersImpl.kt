package com.repzone.domain.document.service

import com.repzone.core.enums.UserSelectionType
import com.repzone.core.model.module.base.IDocumentBaseParameters
import com.repzone.domain.document.IDocumentParameters
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncWarehouseModel
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IWareHouseRepository

class DocumentParametersImpl(private val iDocumentMapRepository: IDocumentMapRepository,
                             private val iMobileModuleParameterRepository: IMobileModuleParameterRepository,
                             private val iWarehouseRepository: IWareHouseRepository
): IDocumentParameters {
    //region Field
    private var documentType: DocumentType? = null
    private var currentCustomer: SyncCustomerModel? = null
    private var documentMapModel: SyncDocumentMapModel? = null
    private var documentParameters: IDocumentBaseParameters? = null
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override suspend fun preLoadDocumentParameters(documentType: DocumentType, customer: SyncCustomerModel, documentId: Long) {
        this.documentType = documentType
        currentCustomer = customer
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

    override suspend fun getWareHouse(): SyncWarehouseModel? {
        if(getDocumentParameters().isActive && getDocumentParameters().showWarehouseSelection != UserSelectionType.No){
            val warehouseModel  = iWarehouseRepository.getWarehouseList(currentCustomer!!.organizationId?.toInt() ?: 0, documentMapModel!!.operationType)
            return warehouseModel.firstOrNull()
        }else{
            return null
        }
    }
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
}