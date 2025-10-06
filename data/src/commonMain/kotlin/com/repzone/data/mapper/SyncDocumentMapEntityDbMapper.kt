package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.domain.model.SyncDocumentMapModel

class SyncDocumentMapEntityDbMapper : Mapper<SyncDocumentMapEntity, SyncDocumentMapModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

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
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
