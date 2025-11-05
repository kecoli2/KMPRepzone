package com.repzone.data.repository.imp

import com.repzone.core.enums.DocProcessType
import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.NumberTemplateType
import com.repzone.core.enums.PrinterDeviceType
import com.repzone.core.model.PrinterListItem
import com.repzone.data.mapper.PrinterDocumentRelationInformationEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessStepEntityDbMapper
import com.repzone.database.PrinterDocumentRelationInformationEntity
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.database.SyncDocumentMapProcessEntity
import com.repzone.database.SyncDocumentMapProcessStepEntity
import com.repzone.database.SyncDocumentOrganizationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.count
import com.repzone.database.runtime.insertOrReplace
import com.repzone.database.runtime.rawExecute
import com.repzone.database.runtime.select
import com.repzone.domain.model.DocumentMapDocNumberInformationModel
import com.repzone.domain.model.DocumentNumberAndPrefix
import com.repzone.domain.model.LastDocumentModel
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel
import com.repzone.domain.repository.IDocumentMapRepository

class DocumentMapRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                                private val mapperDocumentMap: SyncDocumentMapEntityDbMapper,
                                private val mapperDocumentProcess: SyncDocumentMapProcessEntityDbMapper,
                                private val mapperDcomentMapProcessStep: SyncDocumentMapProcessStepEntityDbMapper,
    private val mapperPrinterDocumentRelationInformationEntity: PrinterDocumentRelationInformationEntityDbMapper
): IDocumentMapRepository {

    //region Public Method
    override suspend fun getAll(orgId: Int): List<SyncDocumentMapModel> {
        val list = iDatabaseManager.getSqlDriver().select<SyncDocumentMapEntity> {
            innerJoin<SyncDocumentOrganizationEntity>("DocumentTypeId","Id"){
                alias("DOM")
            }

            where {
                criteria("DOM.OrganizationId", equal = orgId)
                criteria("DOM.State", notEqual = 4)
                criteria("SyncDocumentMapEntity.State", notEqual = 4)
            }
        }.toList()

        return list.map {
            mapperDocumentMap.toDomain(it)
        }
    }

    override suspend fun get(name: String, orgId: Int): SyncDocumentMapModel {
        val document = iDatabaseManager.getSqlDriver().select<SyncDocumentMapEntity> {
            where {
                criteria("Name", equal = name)
            }
        }.first()

        val docOrgModel = iDatabaseManager.getSqlDriver().select<SyncDocumentOrganizationEntity> {
            where {
                criteria("DocumentTypeId" , document.Id)
                criteria("State", notEqual = 4)
                criteria("OrganizationId", orgId)
            }
        }.firstOrNull()

        docOrgModel?.let {
            document.copy(
                MinMaxControl = it.MinMaxControl,
                Lang = it.Lang,
                LogoSelection = it.LogoSelection,
                LogoPathUrl = it.LogoPathUrl,
                UseFinancialLogo = it.UseFinancialLogo,
                DocumentHeader = it.DocumentHeader,
                UniqueIdCaption = it.UniqueIdCaption,
                Note = it.Note,
                PrinterTemplatePath = it.PrinterTemplatePath
            )
        }
        return mapperDocumentMap.toDomain(document)
    }

    override suspend fun get(id: Int, orgId: Int): SyncDocumentMapModel {
        val document = iDatabaseManager.getSqlDriver().select<SyncDocumentMapEntity> {
            where {
                criteria("Id", id)
            }
        }.first()

        val docOrgModel = iDatabaseManager.getSqlDriver().select<SyncDocumentOrganizationEntity> {
            where {
                criteria("DocumentTypeId", document.Id)
                criteria("State", notEqual = 4)
                criteria("OrganizationId", orgId)
            }
        }.firstOrNull()

        docOrgModel?.let {
            document.copy(
                MinMaxControl = it.MinMaxControl,
                Lang = it.Lang,
                LogoSelection = it.LogoSelection,
                LogoPathUrl = it.LogoPathUrl,
                UseFinancialLogo = it.UseFinancialLogo,
                DocumentHeader = it.DocumentHeader,
                UniqueIdCaption = it.UniqueIdCaption,
                Note = it.Note,
                PrinterTemplatePath = it.PrinterTemplateAbsoulutePath
            )
        }
        return mapperDocumentMap.toDomain(document)
    }

    override suspend fun getProcessBy(documentMapName: String, type: DocProcessType): SyncDocumentMapProcessModel? {
        val doc = iDatabaseManager.getSqlDriver().select<SyncDocumentMapProcessEntity> {
            where {
                criteria("DocumentMapName", equal = documentMapName)
                criteria("DocProcessType", type.ordinal)

            }
        }.firstOrNull()
        return doc?.let {
            mapperDocumentProcess.toDomain(it)
        }
    }

    override suspend fun getOrderedStepsOfProcessesById(processId: Int): List<SyncDocumentMapProcessStepModel> {
        return iDatabaseManager.getSqlDriver().select<SyncDocumentMapProcessStepEntity> {
            where {
                criteria("ProcessId", equal = processId)
                criteria("State", notEqual = 4)
            }
            orderBy {
              order("StepOrder")
            }
        }.toList().map {
            mapperDcomentMapProcessStep.toDomain(it)
        }
    }

    override suspend fun doesExistsDocumentMapById(id: Int): Boolean {
        return iDatabaseManager.getSqlDriver().count<SyncDocumentMapEntity> {
            where {
                criteria("Id", equal = id)
            }
        } > 0
    }

    override suspend fun insertOrReplaceDocumentMapWithPrinterAddress(id: Int, address: String, name: String, printerType: Int, paperWidth: Int) {

        val selectedPrinterType = when(printerType){
            1 -> {
                PrinterDeviceType.DOTMATRIX.ordinal.toLong()
            }
            else -> {
                PrinterDeviceType.THERMAL.ordinal.toLong()
            }
        }
        val entity = PrinterDocumentRelationInformationEntity(
            DocumentMapId = id.toLong(),
            PaperWidth = null,
            PrinterAddress = address,
            PrinterName = name,
            PrinterType = selectedPrinterType,
            State = 1
        )
        iDatabaseManager.getSqlDriver().insertOrReplace(entity)
    }

    override suspend fun getAllDocumentTypes(): List<SyncDocumentMapModel> {
        return iDatabaseManager.getSqlDriver().select<SyncDocumentMapEntity> {
            where {
                criteria("State", notEqual = 4)
            }
        }.toList().map {
            mapperDocumentMap.toDomain(it)
        }
    }

    override suspend fun getPrinters(): List<PrinterListItem> {
        var retList = ArrayList<PrinterListItem>()
        val relationList = iDatabaseManager.getSqlDriver().select<PrinterDocumentRelationInformationEntity> {
            where {
                criteria("State", equal = 1)
            }
        }.toList()

        relationList.forEach {
            if(!retList.any { item -> item.printerAdress == it.PrinterAddress }) {
                retList.add(PrinterListItem(it.PrinterAddress,
                    it.PrinterName,
                    ""))
            }
        }

        retList.forEach { printer ->
            val docIds = relationList.filter {
                it.PrinterAddress == printer.printerAdress
            }.map {
                it.DocumentMapId
            }
            val availableDocumentTypes = iDatabaseManager.getSqlDriver().select<SyncDocumentMapEntity> {
                where {
                    criteria("Id", In = docIds)
                    criteria("State", notEqual = 4)

                }
            }.toList().map {
                it.Description
            }
            printer.copy(
                availableDocumentTypes = availableDocumentTypes.joinToString(", ")
            )
        }

        return retList
    }

    override suspend fun getPrinterInfoByDocumentId(id: Int): PrinterDocumentRelationInformationModel? {
        val entity = iDatabaseManager.getSqlDriver().select<PrinterDocumentRelationInformationEntity> {
            where {
                criteria("DocumentMapId", id)
                criteria("State", equal = 1)
            }
        }.firstOrNull()
        return entity?.let {
            mapperPrinterDocumentRelationInformationEntity.toDomain(it)
        }
    }

    override suspend fun clearPrinterRelations(printerAdress: String) {
        iDatabaseManager.getSqlDriver().rawExecute("UPDATE PrinterDocumentRelationInformation SET State = 0 WHERE PrinterAddress = '${printerAdress}'")
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