package com.repzone.data.mapper

import com.repzone.core.enums.PrinterDeviceType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.PrinterDocumentRelationInformationEntity
import com.repzone.domain.model.PrinterDocumentRelationInformationModel

class PrinterDocumentRelationInformationEntityDbMapper : Mapper<PrinterDocumentRelationInformationEntity, PrinterDocumentRelationInformationModel> {
    //region Public Method
    override fun toDomain(from: PrinterDocumentRelationInformationEntity): PrinterDocumentRelationInformationModel {
        return PrinterDocumentRelationInformationModel(
            documentMapId = from.DocumentMapId,
            paperWidth = from.PaperWidth,
            printerAddress = from.PrinterAddress,
            printerName = from.PrinterName,
            printerType = from.PrinterType?.toEnum<PrinterDeviceType>() ?: PrinterDeviceType.DOTMATRIX,
            state = from.State
        )
    }

    override fun fromDomain(domain: PrinterDocumentRelationInformationModel): PrinterDocumentRelationInformationEntity {
        return PrinterDocumentRelationInformationEntity(
            DocumentMapId = domain.documentMapId,
            PaperWidth = domain.paperWidth,
            PrinterAddress = domain.printerAddress,
            PrinterName = domain.printerName,
            PrinterType = domain.printerType.enumToLong(),
            State = domain.state
        )
    }
    //endregion

}
