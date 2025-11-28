package com.repzone.data.repository.imp

import com.repzone.core.enums.DocProcessType
import com.repzone.core.model.PrinterListItem
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.database.SyncDocumentOrganizationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel
import com.repzone.domain.repository.IDistributionService

class DistributionServiceImpl(private val iDatabaseManager: IDatabaseManager): IDistributionService {
    //region Field
    //endregion

    //region Public Method
    override suspend fun getAll(orgId: Int): List<SyncDocumentMapModel> {
        TODO("Not yet implemented")
    }

    override suspend fun get(name: String, orgId: Int): SyncDocumentMapModel? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Int, orgId: Int): SyncDocumentMapModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getProcessBy(documentMapName: String, type: DocProcessType): SyncDocumentMapProcessModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getStepsOfProcessesById(processId: Int): List<SyncDocumentMapProcessStepModel> {
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

    override suspend fun getPrinterInfoByDocumentId(id: Int): PrinterDocumentRelationInformationModel? {
        TODO("Not yet implemented")
    }

    override suspend fun clearPrinterRelations(printerAddress: String) {
        TODO("Not yet implemented")
    }
    //endregion

    //region Private Method
    //endregion

}