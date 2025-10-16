package com.repzone.data.mapper

import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.network.dto.DocumentMapModelDto
import kotlin.time.ExperimentalTime

class SyncDocumentMapEntityDbMapper : MapperDto<SyncDocumentMapEntity, SyncDocumentMapModel, DocumentMapModelDto> {
    //region Public Method
    override fun toDomain(from: SyncDocumentMapEntity): SyncDocumentMapModel {
        return SyncDocumentMapModel(
            id = from.Id,
            description = from.Description,
            documentHeader = from.DocumentHeader,
            documentTypeGroup = from.DocumentTypeGroup,
            ioType = from.IoType,
            isElectronicDocument = from.IsElectronicDocument,
            isFulfillment = from.IsFulfillment,
            lang = from.Lang,
            logoPathUrl = from.LogoPathUrl,
            logoSelection = from.LogoSelection,
            minMaxControl = from.MinMaxControl,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            note = from.Note,
            operationType = from.OperationType,
            printerTemplatePath = from.PrinterTemplatePath,
            printQrCode = from.PrintQrCode,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            uniqueIdCaption = from.UniqueIdCaption,
            useFinancialLogo = from.UseFinancialLogo,
            warehouseType = from.WarehouseType
        )
    }

    override fun fromDomain(domain: SyncDocumentMapModel): SyncDocumentMapEntity {
        return SyncDocumentMapEntity(
            Id = domain.id,
            Description = domain.description,
            DocumentHeader = domain.documentHeader,
            DocumentTypeGroup = domain.documentTypeGroup,
            IoType = domain.ioType,
            IsElectronicDocument = domain.isElectronicDocument,
            IsFulfillment = domain.isFulfillment,
            Lang = domain.lang,
            LogoPathUrl = domain.logoPathUrl,
            LogoSelection = domain.logoSelection,
            MinMaxControl = domain.minMaxControl,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            Note = domain.note,
            OperationType = domain.operationType,
            PrinterTemplatePath = domain.printerTemplatePath,
            PrintQrCode = domain.printQrCode,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            UniqueIdCaption = domain.uniqueIdCaption,
            UseFinancialLogo = domain.useFinancialLogo,
            WarehouseType = domain.warehouseType
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: DocumentMapModelDto): SyncDocumentMapEntity {
        return SyncDocumentMapEntity(
            Id = dto.id.toLong(),
            Description = dto.description,
            DocumentHeader = null,
            DocumentTypeGroup = dto.documentTypeGroup?.enumToLong(),
            IoType = dto.ioType?.enumToLong(),
            IsElectronicDocument = dto.isElectronicDocument.toLong(),
            IsFulfillment = null,
            Lang = null,
            LogoPathUrl = null,
            LogoSelection = null,
            MinMaxControl = dto.minMaxControl.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            Note = null,
            OperationType = dto.operationType?.enumToLong(),
            PrinterTemplatePath = null,
            PrintQrCode = null,
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            UniqueIdCaption = null,
            UseFinancialLogo = null,
            WarehouseType = dto.warehouseType?.enumToLong()
        )
    }
    //endregion

}
