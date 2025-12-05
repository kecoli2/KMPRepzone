package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentOrganizationEntity
import com.repzone.domain.model.SyncDocumentOrganizationModel
import com.repzone.network.dto.DocumentMapDocumentOrganizationDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncDocumentOrganizationEntityDbMapper : MapperDto<SyncDocumentOrganizationEntity, SyncDocumentOrganizationModel, DocumentMapDocumentOrganizationDto> {
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
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
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
            State = domain.state.enumToLong(),
            UniqueIdCaption = domain.uniqueIdCaption,
            UseFinancialLogo = domain.useFinancialLogo
        )
    }

    override fun fromDto(dto: DocumentMapDocumentOrganizationDto): SyncDocumentOrganizationEntity {
        return SyncDocumentOrganizationEntity(
            Id = dto.id.toLong(),
            DocumentHeader = dto.documentHeader,
            DocumentTypeId = dto.documentTypeId?.toLong(),
            Lang = dto.lang,
            LogoPathUrl = dto.logoPathUrl,
            LogoSelection = dto.logoSelection?.toLong(),
            MinMaxControl = dto.minMaxControl.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Note = dto.note,
            OrganizationId = dto.organizationId?.toLong(),
            PrinterTemplateAbsoulutePath = dto.printerTemplateAbsolutePath,
            PrinterTemplatePath = dto.printerTemplatePath,
            PrintQrCode = dto.printQrCode.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            UniqueIdCaption = dto.uniqueIdCaption,
            UseFinancialLogo = dto.useFinancialLogo.toLong()
        )
    }
    //endregion

}
