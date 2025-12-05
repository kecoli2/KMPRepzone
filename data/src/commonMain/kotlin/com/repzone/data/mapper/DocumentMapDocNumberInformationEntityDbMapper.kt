package com.repzone.data.mapper

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.DocumentMapDocNumberInformationEntity
import com.repzone.domain.model.DocumentMapDocNumberInformationModel

class DocumentMapDocNumberInformationEntityDbMapper : Mapper<DocumentMapDocNumberInformationEntity, DocumentMapDocNumberInformationModel> {
    //region Public Method
    override fun toDomain(from: DocumentMapDocNumberInformationEntity): DocumentMapDocNumberInformationModel {
        return DocumentMapDocNumberInformationModel(
            id = from.Id,
            documentMapId = from.DocumentMapId,
            documentNumberBody = from.DocumentNumberBody,
            documentNumberPostfix = from.DocumentNumberPostfix,
            documentNumberPrefix = from.DocumentNumberPrefix,
            documentType = from.DocumentType?.toEnum<DocumentActionType>() ?: DocumentActionType.EMPTY,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
        )
    }

    override fun fromDomain(domain: DocumentMapDocNumberInformationModel): DocumentMapDocNumberInformationEntity {
        return DocumentMapDocNumberInformationEntity(
            Id = domain.id,
            DocumentMapId = domain.documentMapId,
            DocumentNumberBody = domain.documentNumberBody,
            DocumentNumberPostfix = domain.documentNumberPostfix,
            DocumentNumberPrefix = domain.documentNumberPrefix,
            DocumentType = domain.documentType.enumToLong(),
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
