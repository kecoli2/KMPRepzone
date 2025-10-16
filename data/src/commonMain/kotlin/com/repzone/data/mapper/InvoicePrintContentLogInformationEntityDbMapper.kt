package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.InvoicePrintContentLogInformationEntity
import com.repzone.domain.model.InvoicePrintContentLogInformationModel

class InvoicePrintContentLogInformationEntityDbMapper : Mapper<InvoicePrintContentLogInformationEntity, InvoicePrintContentLogInformationModel> {
    //region Public Method
    override fun toDomain(from: InvoicePrintContentLogInformationEntity): InvoicePrintContentLogInformationModel {
        return InvoicePrintContentLogInformationModel(
            id = from.Id,
            printContent = from.PrintContent,
            sessionId = from.SessionId
        )
    }

    override fun fromDomain(domain: InvoicePrintContentLogInformationModel): InvoicePrintContentLogInformationEntity {
        return InvoicePrintContentLogInformationEntity(
            Id = domain.id,
            PrintContent = domain.printContent,
            SessionId = domain.sessionId
        )
    }
    //endregion

}
