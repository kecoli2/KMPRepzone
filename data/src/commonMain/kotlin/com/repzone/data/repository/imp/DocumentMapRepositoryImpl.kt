package com.repzone.data.repository.imp

import com.repzone.core.enums.DocProcessType
import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.NumberTemplateType
import com.repzone.core.model.PrinterListItem
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.DocumentMapDocNumberInformationModel
import com.repzone.domain.model.DocumentNumberAndPrefix
import com.repzone.domain.model.LastDocumentModel
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel
import com.repzone.domain.repository.IDocumentMapRepository

class DocumentMapRepositoryImpl(iDatabaseManager: IDatabaseManager): IDocumentMapRepository {

    //region Public Method
    override suspend fun getAll(orgId: Int): List<SyncDocumentMapModel> {
        TODO("Not yet implemented")
    }

    override suspend fun get(name: String, orgId: Int): SyncDocumentMapModel {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Int, orgId: Int): SyncDocumentMapModel {
        TODO("Not yet implemented")
    }

    override suspend fun getProcessBy(documentMapName: String, type: DocProcessType): SyncDocumentMapProcessModel {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderedStepsOfProcessesById(processId: Int): List<SyncDocumentMapProcessStepModel> {
        TODO("Not yet implemented")
    }

    override suspend fun doesExistsDocumentMapById(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrReplaceDocumentMapWithPrinterAddress(id: Int, address: String, name: String, printerType: Int, paperWidth: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDocumentTypes(): List<SyncDocumentMapModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getPrinters(): List<PrinterListItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getPrinterInfoByDocumentId(id: Int): PrinterDocumentRelationInformationModel {
        TODO("Not yet implemented")
    }

    override suspend fun clearPrinterRelations(printerAdress: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAnyPrinterInfo(): PrinterDocumentRelationInformationModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getDocNumberByDocumentMapId(documentMapId: Int): DocumentMapDocNumberInformationModel {
        TODO("Not yet implemented")
    }

    override suspend fun getDocNumberByDocumentGroup(documentGroup: DocumentTypeGroup): DocumentMapDocNumberInformationModel {
        TODO("Not yet implemented")
    }

    override suspend fun setDocumentNumberByDocumentGroup(documentGroup: DocumentTypeGroup, prefix: String, number: Int, postfix: String, documentMapId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun logInvoicePrintContent(content: String, sessionId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logCollectionPrintContent(content: String, sessionId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getADocumentNumberFromAPI(docGroupType: Int, docTypeId: Int, numberTemplateType: NumberTemplateType) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchLastDocumentNumber(docGroupType: Int, docTypeId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun prepareDocNumber(docModel: LastDocumentModel): DocumentNumberAndPrefix {
        TODO("Not yet implemented")
    }

    override suspend fun softDeleteDocumentMapNumber(docNumber: DocumentMapDocNumberInformationModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDocumentFromAPI(documentGroup: DocumentTypeGroup, documentUniqueId: String): String {
        TODO("Not yet implemented")
    }
    //endregion
}