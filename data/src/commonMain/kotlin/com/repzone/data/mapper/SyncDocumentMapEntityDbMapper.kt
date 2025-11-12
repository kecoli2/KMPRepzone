package com.repzone.data.mapper

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.IoType
import com.repzone.core.enums.SalesOperationType
import com.repzone.core.enums.WarehouseType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toInstant
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.network.dto.DocumentMapModelDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncDocumentMapEntityDbMapper : MapperDto<SyncDocumentMapEntity, SyncDocumentMapModel, DocumentMapModelDto> {
    //region Public Method
    override fun toDomain(from: SyncDocumentMapEntity): SyncDocumentMapModel {
        return SyncDocumentMapModel(
            id = from.Id,
            description = from.Description,
            documentHeader = from.DocumentHeader,
            documentTypeGroup = from.DocumentTypeGroup?.toEnum<DocumentActionType>() ?: DocumentActionType.EMPTY,
            ioType = from.IoType?.toEnum<IoType>() ?: IoType.EMPTY,
            isElectronicDocument = from.IsElectronicDocument?.toBoolean() ?: false,
            isFulfillment = from.IsFulfillment?.toBoolean() ?: false,
            lang = from.Lang,
            logoPathUrl = from.LogoPathUrl,
            logoSelection = from.LogoSelection?.toInt(),
            minMaxControl = from.MinMaxControl?.toBoolean() ?: false,
            modificationDateUtc = from.ModificationDateUtc?.toInstant(),
            name = from.Name,
            note = from.Note,
            operationType = from.OperationType?.toEnum<SalesOperationType>() ?: SalesOperationType.EMPTY,
            printerTemplatePath = from.PrinterTemplatePath,
            printQrCode = from.PrintQrCode?.toBoolean() ?: false,
            recordDateUtc = from.RecordDateUtc?.toInstant() ?: now().toInstant(),
            state = from.State,
            uniqueIdCaption = from.UniqueIdCaption,
            useFinancialLogo = from.UseFinancialLogo?.toBoolean() ?: false,
            warehouseType = from.WarehouseType?.toEnum<WarehouseType>() ?: WarehouseType.EMPTY
        )
    }

    override fun fromDomain(domain: SyncDocumentMapModel): SyncDocumentMapEntity {
        return SyncDocumentMapEntity(
            Id = domain.id,
            Description = domain.description,
            DocumentHeader = domain.documentHeader,
            DocumentTypeGroup = domain.documentTypeGroup.enumToLong(),
            IoType = domain.ioType.enumToLong(),
            IsElectronicDocument = domain.isElectronicDocument.toLong(),
            IsFulfillment = domain.isFulfillment.toLong(),
            Lang = domain.lang,
            LogoPathUrl = domain.logoPathUrl,
            LogoSelection = domain.logoSelection?.toLong(),
            MinMaxControl = domain.minMaxControl.toLong(),
            ModificationDateUtc = domain.modificationDateUtc?.toEpochMilliseconds(),
            Name = domain.name,
            Note = domain.note,
            OperationType = domain.operationType.enumToLong(),
            PrinterTemplatePath = domain.printerTemplatePath,
            PrintQrCode = domain.printQrCode.toLong(),
            RecordDateUtc = domain.recordDateUtc.toEpochMilliseconds(),
            State = domain.state,
            UniqueIdCaption = domain.uniqueIdCaption,
            UseFinancialLogo = domain.useFinancialLogo.toLong(),
            WarehouseType = domain.warehouseType.enumToLong()
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
