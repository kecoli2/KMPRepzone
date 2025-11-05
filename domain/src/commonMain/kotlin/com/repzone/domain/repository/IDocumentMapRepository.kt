package com.repzone.domain.repository

import com.repzone.core.enums.DocProcessType
import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.NumberTemplateType
import com.repzone.core.model.PrinterListItem
import com.repzone.domain.model.DocumentMapDocNumberInformationModel
import com.repzone.domain.model.DocumentNumberAndPrefix
import com.repzone.domain.model.LastDocumentModel
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel

interface IDocumentMapRepository {
    suspend fun getAll(orgId: Int): List<SyncDocumentMapModel>
    suspend fun get(name: String, orgId: Int) : SyncDocumentMapModel
    suspend fun get(id: Int, orgId: Int) : SyncDocumentMapModel
    suspend fun getProcessBy(documentMapName: String, type: DocProcessType): SyncDocumentMapProcessModel?
    suspend fun getOrderedStepsOfProcessesById(processId: Int): List<SyncDocumentMapProcessStepModel>
    suspend fun doesExistsDocumentMapById(id: Int): Boolean
    suspend fun insertOrReplaceDocumentMapWithPrinterAddress(id : Int, address: String, name: String,printerType : Int, paperWidth: Int)
    suspend fun getAllDocumentTypes(): List<SyncDocumentMapModel>
    suspend fun getPrinters(): List<PrinterListItem>
    suspend fun getPrinterInfoByDocumentId(id: Int): PrinterDocumentRelationInformationModel?
    suspend fun clearPrinterRelations(printerAdress: String)
    suspend fun getAnyPrinterInfo(): PrinterDocumentRelationInformationModel?
    suspend fun getDocNumberByDocumentMapId(documentMapId: Int): DocumentMapDocNumberInformationModel
    suspend fun getDocNumberByDocumentGroup(documentGroup: DocumentTypeGroup): DocumentMapDocNumberInformationModel
    suspend fun setDocumentNumberByDocumentGroup(documentGroup: DocumentTypeGroup ,prefix: String, number: Int, postfix: String, documentMapId: Int = 0)
    suspend fun logInvoicePrintContent(content: String, sessionId: String)
    suspend fun logCollectionPrintContent(content: String, sessionId: String)
    suspend fun getADocumentNumberFromAPI(docGroupType: Int,docTypeId: Int, numberTemplateType: NumberTemplateType)
    suspend fun fetchLastDocumentNumber(docGroupType: Int,docTypeId: Int)
    suspend fun prepareDocNumber(docModel: LastDocumentModel): DocumentNumberAndPrefix
    suspend fun softDeleteDocumentMapNumber(docNumber: DocumentMapDocNumberInformationModel)
    suspend fun deleteDocumentFromAPI(documentGroup: DocumentTypeGroup, documentUniqueId: String): String
}