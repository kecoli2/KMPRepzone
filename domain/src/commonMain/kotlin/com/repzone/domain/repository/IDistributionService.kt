package com.repzone.domain.repository

import com.repzone.core.enums.DocProcessType
import com.repzone.core.model.PrinterListItem
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel

interface IDistributionService {

    suspend fun getAll(orgId: Int): List<SyncDocumentMapModel>

    suspend fun get(name: String, orgId: Int): SyncDocumentMapModel?

    suspend fun get(id: Int, orgId: Int): SyncDocumentMapModel?

    suspend fun getProcessBy(documentMapName: String, type: DocProcessType): SyncDocumentMapProcessModel?

    suspend fun getStepsOfProcessesById(processId: Int): List<SyncDocumentMapProcessStepModel>

    suspend fun doesExistsDocumentMapById(id: Int): Boolean

    suspend fun insertOrReplaceDocumentMapWithPrinterAddress(id: Int, address: String, name: String, printerType: Int, paperWidth: Int)

    suspend fun getAllDocumentTypes(): List<SyncDocumentMapModel>

    suspend fun getPrinters(): List<PrinterListItem>

    suspend fun getPrinterInfoByDocumentId(id: Int): PrinterDocumentRelationInformationModel?

    suspend fun clearPrinterRelations(printerAddress: String)
}