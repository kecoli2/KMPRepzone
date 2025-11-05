package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CollectionPrintContentLogInformationEntity
import com.repzone.domain.model.CollectionPrintContentLogInformationModel

class CollectionPrintContentLogInformationEntityDbMapper : Mapper<CollectionPrintContentLogInformationEntity, CollectionPrintContentLogInformationModel> {
    //region Public Method
    override fun toDomain(from: CollectionPrintContentLogInformationEntity): CollectionPrintContentLogInformationModel {
        return CollectionPrintContentLogInformationModel(
            id = from.Id,
            sessionId = from.SessionId,
            printContent = from.PrintContent
        )
    }

    override fun fromDomain(domain: CollectionPrintContentLogInformationModel): CollectionPrintContentLogInformationEntity {
        return CollectionPrintContentLogInformationEntity(
            Id = domain.id,
            SessionId = domain.sessionId,
            PrintContent = domain.printContent
        )
    }
    //endregion

}
