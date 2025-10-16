package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncDocumentOrganizationEntity
import com.repzone.domain.model.SyncDocumentOrganizationModel

class SyncDocumentOrganizationEntityDbMapper : Mapper<SyncDocumentOrganizationEntity, SyncDocumentOrganizationModel> {
    //region Public Method
    override fun toDomain(from: SyncDocumentOrganizationEntity): SyncDocumentOrganizationModel {
        return SyncDocumentOrganizationModel(
            id = from.Id,
            documentHeader = from.DocumentHeader,
            documentTypeId = from.DocumentTypeId,
            lang = from.Lang,
            logoPathUrl = from.LogoPathUrl,
            logoSelection = from.LogoSelection,
            minMaxControl = from.MinMaxControl,
            modificationDateUtc = from.ModificationDateUtc,
            note = from.Note,
            organizationId = from.OrganizationId,
            printerTemplateAbsoulutePath = from.PrinterTemplateAbsoulutePath,
            printerTemplatePath = from.PrinterTemplatePath,
            printQrCode = from.PrintQrCode,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            uniqueIdCaption = from.UniqueIdCaption,
            useFinancialLogo = from.UseFinancialLogo
        )
    }

    override fun fromDomain(domain: SyncDocumentOrganizationModel): SyncDocumentOrganizationEntity {
        return SyncDocumentOrganizationEntity(
            Id = domain.id,
            DocumentHeader = domain.documentHeader,
            DocumentTypeId = domain.documentTypeId,
            Lang = domain.lang,
            LogoPathUrl = domain.logoPathUrl,
            LogoSelection = domain.logoSelection,
            MinMaxControl = domain.minMaxControl,
            ModificationDateUtc = domain.modificationDateUtc,
            Note = domain.note,
            OrganizationId = domain.organizationId,
            PrinterTemplateAbsoulutePath = domain.printerTemplateAbsoulutePath,
            PrinterTemplatePath = domain.printerTemplatePath,
            PrintQrCode = domain.printQrCode,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            UniqueIdCaption = domain.uniqueIdCaption,
            UseFinancialLogo = domain.useFinancialLogo
        )
    }
    //endregion

}
